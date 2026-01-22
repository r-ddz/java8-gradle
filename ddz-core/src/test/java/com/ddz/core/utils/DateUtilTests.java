package com.ddz.core.utils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class DateUtilTests {

    @Test
    public void createDateAddMinute1() {
        Date date = DateUtil.createDateAddMinute(5);
        System.out.println(DateUtil.toStr(date));
    }

    @Test
    public void createDateAddMinute2() {
        Date date = DateUtil.createDateAddMinute(-70);
        System.out.println(DateUtil.toStr(date));
    }



    @Test
    public void toDate1() {
        Date date = DateUtil.toDate("2026-01-21 16:22:00");
        System.out.println(DateUtil.toStr(date));
        System.out.println(DateUtil.toStrByFormat(date, DateUtil.DEFAULT_DATE_FORMAT));
    }

    @Test
    public void toDate2() {
        Date date = DateUtil.toDateByFormat("2026-01-21", DateUtil.DEFAULT_DATE_FORMAT);
        System.out.println(DateUtil.toStr(date));
        System.out.println(DateUtil.toStrByFormat(date, DateUtil.DEFAULT_DATE_FORMAT));
    }




    @BeforeAll
    public static void before(){
        System.out.println("================================= 测试开始 =================================");
    }

    @AfterAll
    public static void after(){
        System.out.println("================================= 测试结束 =================================");
    }

}
