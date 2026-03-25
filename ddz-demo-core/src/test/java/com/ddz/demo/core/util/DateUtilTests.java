package com.ddz.demo.core.util;

import com.ddz.demo.core.model.data.UserDemo;
import com.ddz.demo.core.model.data.UserDemoFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DateUtilTests {

    @Test
    public void daysToSeconds() {
        long a = DateUtil.daysToSeconds(1L);
        System.out.println(a);
    }

    @Test
    public void hoursToSeconds() {
        long a = DateUtil.hoursToSeconds(2L);
        System.out.println(a);
    }


    @BeforeAll
    public static void beforeAll() {
        System.out.println("================================= 测试开始 =================================");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("================================= 测试结束 =================================");
    }

}
