package com.ddz.redis;

import com.ddz.redis.stock.service.DdzStockDemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = RedisApplication.class)
public class RedisTests {

    @Autowired
    private DdzStockDemoService ddzStockDemoService;

    @Test
    void test() {


        String stockId = "12345";
        // 查库存，先从缓存查，没有则初始化
        Long num1 = ddzStockDemoService.getStock(stockId); // 【1000】
        // 直接减少 1200 变为负库存
        Long num2 = ddzStockDemoService.decrement(stockId, 1200L); // 【-200】
        // 直接加 230 库存
        Long num3 = ddzStockDemoService.increment(stockId, 230L); // 【30】

        // 普通分配 50 提示库存不够
        Long num4 = ddzStockDemoService.reduce(stockId, 50L); // 【-2】

        // 部分分配 50 有多少分多少
        Long num5 = ddzStockDemoService.reducePart(stockId, 50L); // 【30】

        // 清空库存 清零
        Long num6 = ddzStockDemoService.reduceAll(stockId); // 【30】

        System.out.println("########## 结束 ##########");
    }


}
