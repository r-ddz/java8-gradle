package com.ddz.demo.core.utils.time;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TimeUnitUtilTests {
    @Test
    public void daysToSeconds() {
        long a = TimeUnitUtil.daysToSeconds(1L);
        System.out.println(a);
    }

    @Test
    public void hoursToSeconds() {
        long a = TimeUnitUtil.hoursToSeconds(2L);
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
