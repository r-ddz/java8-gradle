package com.ddz.demo.core.utils.time;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class LocalDateUtilTests {

    @Test
    public void getStartDay() {
        LocalDate a = LocalDateUtil.getStartDay(2026, 2);
        System.out.println(a);
    }

    @Test
    public void getEndDay() {
        LocalDate a = LocalDateUtil.getEndDay(2026, 2);
        LocalDate b = LocalDateUtil.getEndDay(2026, 3);
        System.out.println(a);
        System.out.println(b);
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
