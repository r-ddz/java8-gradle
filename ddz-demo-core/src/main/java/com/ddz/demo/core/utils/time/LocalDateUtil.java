package com.ddz.demo.core.utils.time;


import java.time.LocalDate;
import java.time.YearMonth;

public class LocalDateUtil {

    /**
     * 获取指定月的第一天
     * @param year
     * @param month
     * @return
     */
    public static LocalDate getStartDay(int year, int month) {
        return LocalDate.of(year, month, 1);
    }

    /**
     * 获取指定月的最后一天
     * @param year
     * @param month
     * @return
     */
    public static LocalDate getEndDay(int year, int month) {
        return YearMonth.of(year, month).atEndOfMonth();
    }

}
