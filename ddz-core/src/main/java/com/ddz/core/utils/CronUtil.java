package com.ddz.core.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.pattern.CronPattern;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * cron表达式工具类
 *
 * @author admin
 * @date 2026/1/21 16:22
 */
public class CronUtil {

    /**
     * 获取下一次执行时间
     *
     * @param cronStr cron表达式
     * @return 下一次执行时间
     */
    public static Calendar getNextCalendar(String cronStr) {
        if (StrUtil.isBlank(cronStr)) {
            return null;
        }
        CronPattern pattern = new CronPattern(cronStr);
        return pattern.nextMatchAfter(Calendar.getInstance());
    }

    /**
     * 获取下下一次执行时间
     *
     * @param cronStr cron表达式
     * @return 下下一次执行时间
     */
    public static Date getNextDate(String cronStr) {
        return Objects.requireNonNull(getNextCalendar(cronStr)).getTime();
    }

    /**
     * 获取多个下下次执行时间
     *
     * @param cronStr cron表达式
     * @param num     数量
     * @return 多个下下次执行时间
     */
    public static List<Date> getNextDates(String cronStr, int num) {
        if (StrUtil.isBlank(cronStr) || num <= 0) {
            return null;
        }
        CronPattern pattern = new CronPattern(cronStr);
        Calendar calendar = Calendar.getInstance();
        List<Date> dates = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            calendar = pattern.nextMatchAfter(calendar);
            dates.add(calendar.getTime());
            // 加一秒，再计算下一次 【hutool工具的判断是大于等于，并且精度是到秒级别】
            calendar.add(Calendar.SECOND, 1);
        }
        return dates;
    }

}
