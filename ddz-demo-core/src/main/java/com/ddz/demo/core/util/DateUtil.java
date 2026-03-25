package com.ddz.demo.core.util;

import java.util.concurrent.TimeUnit;

public class DateUtil {

    /**
     * 天数转换成秒数
     *
     * @param day 天数
     * @return 秒数
     */
    public static long daysToSeconds(long day) {
        return TimeUnit.DAYS.toSeconds(day);
    }

    /**
     * 小时数转换成秒数
     *
     * @param hours 小时数
     * @return 秒数
     */
    public static long hoursToSeconds(long hours) {
        return TimeUnit.HOURS.toSeconds(hours);
    }

}
