package com.ddz.redis.stock.template;


import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 抽象类，主要提供以下方法：
 *
 * getStock(String stockId) ：查库存，先从缓存查，没有则初始化
 * increment(String stockId, long num) ：直接加库存
 * decrement(String stockId, long num) ：直接减库存（不校验够不够，允许负库存）
 * reduce(String stockId, long num) ：lua脚本减库存，return -1 找不到库存 -2 库存不足 >=0 扣减之后的剩余库存
 * reducePart(String stockId, long num) ：lua脚本部分扣减库存，return -1 找不到库存 >=0 本次扣减了多少
 * reduceAll(String stockId) ：lua脚本清零库存，return -1 找不到库存 >=0 本次扣减了多少
 *
 * @author ddz
 */
public abstract class DdzStockTemplate implements DdzStockCallback {

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    /** 缓存过期时间(秒) 默认3天【60*60*24*3】 */
    public final static long STOCK_CACHE_TIMEOUT = 259200L;
    /** 安全时间(秒) 默认1天【60*60*24】 缓存有效期小于这个时间进行缓存续期 */
    public final static long STOCK_CACHE_SAFE_TIME = 86400L;
    /** 不安全时间(秒) 默认10秒 缓存有效期小于这个时间直接初始化库存 */
    public final static long STOCK_CACHE_UNSAFE_TIME = 10L;

    /** 锁等待时间 */
    public final static long STOCK_LOCK_WAIT_TIME = 180L;
    /** 锁租赁时间 */
    public final static long STOCK_LOCK_LEASE_TIME = 300L;

    /** 执行扣库存的脚本(标准) */
    public static final String STOCK_LUA_NORM;
    /** 执行扣库存的脚本(部分) */
    public static final String STOCK_LUA_PART;
    /** 执行扣库存的脚本(全部) */
    public static final String STOCK_LUA_ALL;

    static {
        /*
         * 扣减库存Lua脚本 - 标准模式（库存足够才扣减，不够的时候扣减失败）
         *
         * 返回 -1: 找不到库存，库存未初始化到redis
         * 返回 -2: 库存不足
         * 返回>=0: 扣减之后的剩余库存
         */
        StringBuilder stockLuaNorm = new StringBuilder();
        stockLuaNorm.append("if (redis.call('exists', KEYS[1]) == 1) then");
        stockLuaNorm.append("    local stock = tonumber(redis.call('get', KEYS[1]));");
        stockLuaNorm.append("    local num = tonumber(ARGV[1]);");
        stockLuaNorm.append("    if (stock >= num) then");
        stockLuaNorm.append("        return redis.call('incrby', KEYS[1], 0 - num);");
        stockLuaNorm.append("    end;");
        stockLuaNorm.append("    return -2;");
        stockLuaNorm.append("end;");
        stockLuaNorm.append("return -1;");
        STOCK_LUA_NORM = stockLuaNorm.toString();

        /*
         * 扣减库存Lua脚本 - 部分模式（库存有多少扣多少，不够的时候先把当前库存扣减到0，返回本次扣减了多少）
         *
         * 返回 -1: 找不到库存，库存未初始化到redis
         * 返回  0: 当前库存为 0 没有扣减
         * 返回 >0: 本次扣减了多少
         */
        StringBuilder stockLuaPart = new StringBuilder();
        stockLuaPart.append("if (redis.call('exists', KEYS[1]) == 1) then");
        stockLuaPart.append("    local stock = tonumber(redis.call('get', KEYS[1]));");
        stockLuaPart.append("    local num = tonumber(ARGV[1]);");
        stockLuaPart.append("    if (stock <= 0) then");
        stockLuaPart.append("        return 0;");
        stockLuaPart.append("    end;");
        stockLuaPart.append("    if (stock >= num) then");
        stockLuaPart.append("        redis.call('incrby', KEYS[1], 0 - num);");
        stockLuaPart.append("        return num;");
        stockLuaPart.append("    end;");
        stockLuaPart.append("    if (stock < num) then");
        stockLuaPart.append("        redis.call('incrby', KEYS[1], 0 - stock);");
        stockLuaPart.append("        return stock;");
        stockLuaPart.append("    end;");
        stockLuaPart.append("    return -1;");
        stockLuaPart.append("end;");
        stockLuaPart.append("return -1;");
        STOCK_LUA_PART = stockLuaPart.toString();


        /*
         * 扣减库存Lua脚本 - 全部模式（库存有多少扣多少，无请求数量，直接把当前库存扣减到0，返回本次扣减了多少）
         *
         * 返回 -1: 找不到库存，库存未初始化到redis
         * 返回 >=0: 本次（清零）扣减了多少库存
         */
        StringBuilder stockLuaAll = new StringBuilder();
        stockLuaAll.append("if (redis.call('exists', KEYS[1]) == 1) then");
        stockLuaAll.append("    local stock = tonumber(redis.call('get', KEYS[1]));");
        stockLuaAll.append("    if (stock <= 0) then");
        stockLuaAll.append("        return 0;");
        stockLuaAll.append("    end;");
        stockLuaAll.append("    if (stock > 0) then");
        stockLuaAll.append("        redis.call('incrby', KEYS[1], 0 - stock);");
        stockLuaAll.append("        return stock;");
        stockLuaAll.append("    end;");
        stockLuaAll.append("    return -1;");
        stockLuaAll.append("end;");
        stockLuaAll.append("return -1;");
        STOCK_LUA_ALL = stockLuaAll.toString();
    }

    /**
     * 扣减库存Lua脚本 - 标准模式（库存足够才扣减，不够的时候扣减失败）
     *
     * @param stockId 库存id
     * @param num 数量
     * @return -1 找不到库存 -2 库存不足 >=0 扣减之后的剩余库存
     */
    public Long reduce(String stockId, long num) {
        return reduce(stockId, STOCK_LUA_NORM, num);
    }

    /**
     * 扣减库存Lua脚本 - 部分模式（库存有多少扣多少，不够的时候先把当前库存扣减到0，返回本次扣减了多少）
     *
     * @param stockId 库存id
     * @param num 数量
     * @return -1 找不到库存 >=0 本次扣减了多少
     */
    public Long reducePart(String stockId, long num) {
        return reduce(stockId, STOCK_LUA_PART, num);
    }

    /**
     * 扣减库存Lua脚本 - 全部模式（库存有多少扣多少，无请求数量，直接把当前库存扣减到0，返回本次扣减了多少）
     *
     * @param stockId 库存id
     * @return -1 找不到库存 >=0 本次（清零）扣减了多少库存
     */
    public Long reduceAll(String stockId) {
        return reduce(stockId, STOCK_LUA_ALL);
    }

    /**
     * 执行 lua 脚本扣减库存
     *
     * @param stockId 库存id
     * @param lua lua 脚本
     * @param num 数量
     * @return
     */
    private Long reduce(String stockId, String lua, long num) {
        if (tryInitStock(stockId)) {
            //构建redisScript对象,构造方法参数1 执行的lua脚本   参数2 结果返回类型
            DefaultRedisScript<Long> defaultRedisScript = new DefaultRedisScript<>(lua, Long.class);
            //参数1 redisScript对象  参数2 keys,可以是多个,取决于你lua里的业务, 参数3 args 需要给lua传入的参数 也是多个
            return stringRedisTemplate.execute(defaultRedisScript, Collections.singletonList(getCacheKey(stockId)), Long.toString(num));
        }
        return null;
    }

    /**
     * 执行 lua 脚本扣减库存
     *
     * @param stockId 库存id
     * @param lua lua 脚本
     * @return
     */
    private Long reduce(String stockId, String lua) {
        if (tryInitStock(stockId)) {
            //构建redisScript对象,构造方法参数1 执行的lua脚本   参数2 结果返回类型
            DefaultRedisScript<Long> defaultRedisScript = new DefaultRedisScript<>(lua, Long.class);
            //参数1 redisScript对象  参数2 keys,可以是多个,取决于你lua里的业务, 参数3 args 需要给lua传入的参数 也是多个
            return stringRedisTemplate.execute(defaultRedisScript, Collections.singletonList(getCacheKey(stockId)));
        }
        return null;
    }

    /**
     * 查库存，先从缓存查，没有则初始化
     *
     * @param stockId 库存 ID
     * @return 数量，没有则 return null
     */
    public Long getStock(String stockId) {
        Long num = getStockCache(stockId);
        if (num != null) {
            return num;
        }
        // 缓存没值就初始化
        if (tryInitStock(stockId)) {
            return getStockCache(stockId);
        }
        return null;
    }

    /**
     * 直接加库存
     *
     * @param stockId 库存id
     * @param num 数量
     * @return 加完之后的库存值，查不到库存则 return null
     */
    public Long increment(String stockId, long num) {
        if (tryInitStock(stockId)) {
            return redisTemplate.opsForValue().increment(getCacheKey(stockId), num);
        }
        return null;
    }

    /**
     * 直接减库存（不校验够不够，允许负库存）
     *
     * @param stockId 库存id
     * @param num 数量
     * @return 减完之后的库存值，查不到库存则 return null
     */
    public Long decrement(String stockId, long num) {
        if (tryInitStock(stockId)) {
            return redisTemplate.opsForValue().decrement(getCacheKey(stockId), num);
        }
        return null;
    }

    /**
     * 初始化库存的缓存，从数据库取值
     *
     * @param stockId 库存id
     * @return 库存数量，未查到返回 null
     */
    private Long initStock(String stockId) {
        // 从数据库取值，未查到返回 null
        Long num = getStockByDb(stockId);
        if (null != num) {
            setStockCache(stockId, num);
        }
        return num;
    }

    /**
     * 尝试库存初始化（缓存续期）加锁防并发
     *
     * 剩余时间：不安全直接初始化缓存
     * 剩余时间：安全则续个期
     * 不直接使用 hasKey(K key) 判断缓存的存在是为了保证不会在动库存的一瞬间正好缓存过期
     *
     * @param stockId 库存id
     * @return
     */
    private Boolean tryInitStock(String stockId) {
        Long time = getCacheExpire(stockId);
        if (time >= STOCK_CACHE_SAFE_TIME) {
            return true; // 过期时间充裕，安全
        }
        // 剩余时间不安全直接初始化缓存
        if (time < STOCK_CACHE_UNSAFE_TIME) {
            RLock lock = tryLock(stockId);
            if (lock == null) {
                return false; // 获取锁失败
            }
            try {
                time = getCacheExpire(stockId);
                if (time >= STOCK_CACHE_SAFE_TIME) {
                    return true; // 过期时间充裕，安全
                }
                // 剩余时间不安全直接初始化缓存，安全则续个期
                if (time < STOCK_CACHE_UNSAFE_TIME) {
                    return initStock(stockId) != null;
                }
            }catch(Exception e) {
                deleteCache(stockId); // 如果异常了，保证数据准确性，这里删除缓存，使缓存重置
                return false;
            }finally {
                tryUnLock(lock);
            }
        }
        // 剩余时间还比较安全则直接续个期
        return renewalCache(stockId);
    }

    /**
     * 给库存做缓存
     *
     * @param stockId 库存 ID
     * @param num 库存数量
     */
    private void setStockCache(String stockId, Long num) {
        redisTemplate.opsForValue().set(getCacheKey(stockId), num, STOCK_CACHE_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * 从缓存里查库存
     *
     * @param stockId 库存 ID
     * @return 数量，没有则 return null
     */
    private Long getStockCache(String stockId) {
        // 注意：redis 会把小于 int 最大值的转成 Integer 对象
        Number num = (Number) redisTemplate.opsForValue().get(getCacheKey(stockId));
        return num == null ? null : num.longValue();
    }

    /**
     * 获取缓存过期时间
     *
     * @param stockId 库存 ID
     * @return
     */
    private Long getCacheExpire(String stockId) {
        return redisTemplate.getExpire(getCacheKey(stockId), TimeUnit.SECONDS);
    }

    /**
     * 给缓存设置过期时间（可用于缓存续期）
     *
     * @param stockId 库存 ID
     * @return
     */
    private Boolean renewalCache(String stockId) {
        return redisTemplate.expire(getCacheKey(stockId), STOCK_CACHE_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * 删除缓存
     *
     * @param stockId
     * @return
     */
    public Boolean deleteCache(String stockId) {
        return redisTemplate.delete(getCacheKey(stockId));
    }

    /**
     * 加锁
     *
     * @param stockId 库存 ID
     * @return
     */
    public RLock tryLock(String stockId) {
        try {
            RLock lock = redissonClient.getLock(getLockKey(stockId));
            if (lock.tryLock(STOCK_LOCK_WAIT_TIME, STOCK_LOCK_LEASE_TIME, TimeUnit.SECONDS)) {
                return lock;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解锁
     *
     * @param lock 当前锁对象
     */
    public void tryUnLock(RLock lock) {
        if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

}
