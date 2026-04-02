package com.ddz.demo.core.utils.time;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

public class YearMonthUtilTests {



    @Test
    public void parse() {

        YearMonth yearMonth = YearMonthUtil.parse("202603112", "yyyyMM");

        System.out.println(yearMonth);
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
