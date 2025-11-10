package com.ddz.redis.stock.template;

/**
 * 回调接口
 *
 * @author ddz
 */
public interface DdzStockCallback {

    /**
     * 查数据库的库存数量
     *
     * @param stockId 库存id
     * @return 库存数量，未查到返回 null
     */
    Long getStockByDb(String stockId);

    /**
     * 缓存 KEY
     *
     * @param stockId 库存 ID
     * @return
     */
    String getCacheKey(String stockId);

    /**
     * 锁 KEY
     *
     * @param stockId 库存 ID
     * @return
     */
    String getLockKey(String stockId);

    /**
     * 重置所有的库存缓存
     */
    void resetStockCache();
}
