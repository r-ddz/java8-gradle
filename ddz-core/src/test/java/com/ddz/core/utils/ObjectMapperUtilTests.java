package com.ddz.core.utils;

import com.ddz.core.email.domain.result.Result;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ObjectMapperUtilTests {

    @Test
    public void test() throws IOException {

        System.out.println("============================ 开始 ============================");

        Map<String, Object> map = new HashMap<>();
        map.put("code", 123);
        map.put("msg", "测试测试");

        Result<?> result = ObjectMapperUtil.JSON.convertValue(map, Result.class);

        System.out.println(result);

        System.out.println("============================ 结束 ============================");
    }




}
