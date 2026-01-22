package com.ddz.core.utils;

import java.util.Date;

public class DateUtil {

    /** 默认时间格式 */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** 默认日期格式 */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 创建指定分钟后的日期
     *
     * @param minute 分钟数 正数加 负数减
     * @return
     */
    public static Date createDateAddMinute(int minute) {
        return cn.hutool.core.date.DateUtil.offsetMinute(new Date(), minute);
    }

    /**
     * 将日期转换成字符串  使用默认格式
     *
     * @param date
     * @return
     */
    public static String toStr(Date date) {
        return toStrByFormat(date, DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * 将日期转换成字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String toStrByFormat(Date date, String format) {
        return cn.hutool.core.date.DateUtil.format(date, format);
    }

    /**
     * 将字符串转换成日期  使用默认格式
     *
     * @param dateStr
     * @return
     */
    public static Date toDate(String dateStr) {
        return toDateByFormat(dateStr, DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * 将字符串转换成日期
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static Date toDateByFormat(String dateStr, String format) {
        return cn.hutool.core.date.DateUtil.parse(dateStr, format);
    }

}
