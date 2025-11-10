package com.ddz.redis.stock.service;

import cn.hutool.core.collection.CollUtil;
import com.ddz.redis.stock.template.DdzStockTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * demo
 *
 * @author ddz
 */
@Service
public class DdzStockDemoService extends DdzStockTemplate {

    /** 缓存 KEY 前缀 */
    private final static String STOCK_CACHE_KEY_PREFIX = "DDZ:STOCK_CACHE_KEY:";
    /** 锁前缀 */
    private final static String STOCK_LOCK_KEY_PREFIX = "DDZ:STOCK_LOCK_KEY:";

    /**
     * 缓存 KEY
     *
     * @param stockId 库存 ID
     * @return
     */
    @Override
    public String getCacheKey(String stockId) {
        return STOCK_CACHE_KEY_PREFIX + stockId;
    }

    /**
     * 锁 KEY
     *
     * @param stockId 库存 ID
     * @return
     */
    @Override
    public String getLockKey(String stockId) {
        return STOCK_LOCK_KEY_PREFIX + stockId;
    }

    /**
     * 重置所有的库存缓存
     */
    @Override
    public void resetStockCache() {
        Set<String> keys = redisTemplate.keys(STOCK_CACHE_KEY_PREFIX + "*");
        if (CollUtil.isNotEmpty(keys)) {
            keys.forEach(key -> redisTemplate.delete(key));
        }
    }

    /**
     * 查数据库的库存数量
     *
     * @param stockId 库存id
     * @return 库存数量，未查到返回 null
     */
    @Override
    public Long getStockByDb(String stockId) {
        // TODO 查数据库，这里伪造一份库存数量
        return 1000L;
    }

}
