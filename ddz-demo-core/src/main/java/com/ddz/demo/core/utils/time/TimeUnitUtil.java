package com.ddz.demo.core.utils.time;

import java.util.concurrent.TimeUnit;

/**
 * 时间单位转换工具类
 *
 * @author ddz
 */
public class TimeUnitUtil {

    /**
     * 天数转换成秒数
     * 1天 = 24小时 = 24 * 60 分钟 = 24 * 60 * 60 秒 = 86400 秒
     *
     * @param day 天数
     * @return 秒数
     */
    public static long daysToSeconds(long day) {
        return TimeUnit.DAYS.toSeconds(day);
    }

    /**
     * 小时数转换成秒数
     * 1小时 = 60分钟 = 60 * 60 秒 = 3600 秒
     *
     * @param hours 小时数
     * @return 秒数
     */
    public static long hoursToSeconds(long hours) {
        return TimeUnit.HOURS.toSeconds(hours);
    }
}
