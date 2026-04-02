package com.ddz.demo.core.utils.time;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class LocalDateTimeUtil {

    /**
     * 获取当天开始时间
     * @param date 2026-03-19
     * @return 2026-03-19 00:00:00
     */
    public static LocalDateTime getStartTime(LocalDate date) {
        // 不会修改 date 属性，new 了一个新对象 LocalDateTime
        return date.atStartOfDay();
    }

    /**
     * 获取指定时间，偏移指定时间    （ ***** 注意：偏移会修改传入的对象）
     *
     * @param dateTime 传入的时间
     * @param number 偏移量，正数为向后偏移，负数为向前偏移
     * @return
     */
    public static LocalDateTime offsetDay(LocalDateTime dateTime, long number) {
        // 偏移会修改传入的对象
        return cn.hutool.core.date.LocalDateTimeUtil.offset(dateTime, number, ChronoUnit.DAYS);
    }

}
