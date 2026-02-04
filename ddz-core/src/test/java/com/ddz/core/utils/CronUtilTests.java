package com.ddz.core.utils;

import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CronUtilTests {

    @Test
    public void getNextCalendar() {
        String cronStr = "0/30 * * * * ?";
        Calendar calendar = CronUtil.getNextCalendar(cronStr);
        Date date = calendar.getTime();
        System.out.println(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void getNextDate() {
        String cronStr = "0/30 * * * * ?";
        Date date = CronUtil.getNextDate(cronStr);
        System.out.println(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void getNextDates() {
        String cronStr = "0/30 * * * * ?";
        List<Date> dates = CronUtil.getNextDates(cronStr, 5);
        dates.forEach(date -> System.out.println(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss")));

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
