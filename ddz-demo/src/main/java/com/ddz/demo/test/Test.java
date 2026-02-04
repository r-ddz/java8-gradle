package com.ddz.demo.test;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.pattern.CronPattern;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

public class Test {

//    public static void main(String[] args) {
//
//        System.out.println("=========================================================");
//
//
//        System.out.println(SysModuleEnum.STAFF_BUSINESS_ORG_MANAGEMENT.getCode());
//        System.out.println(SysModuleEnum.STAFF_BUSINESS_ORG_MANAGEMENT.getName());
//        System.out.println(SysModuleEnum.STAFF_BUSINESS_ORG_MANAGEMENT.getDescription());
//
//
//
//
//        System.out.println("=========================================================");
//        System.out.println(SysModuleEnum.STAFF_BUSINESS_ORG_MANAGEMENT);
//        System.out.println(SysModuleEnum.STAFF_BUSINESS_ORG_MANAGEMENT.name());
//        System.out.println("=========================================================");
//    }


//    public static void main(String[] args) {
//
//        System.out.println("=========================================================");
//
//        System.out.println("=========================== for循环 values() ==============================");
//        for (SysModuleEnum sysModuleEnum : SysModuleEnum.values()) {
//            System.out.println(sysModuleEnum);
//            System.out.println(sysModuleEnum.name());
//            System.out.println(sysModuleEnum.getCode());
//            System.out.println(sysModuleEnum.getName());
//            System.out.println(sysModuleEnum.getDescription());
//            System.out.println(sysModuleEnum.ordinal());
//        }
//
//        System.out.println("=========================== Arrays.stream() ==============================");
//        Arrays.stream(SysModuleEnum.values())
//                .forEach(sysModuleEnum -> {
//                    System.out.println(sysModuleEnum.name());
//                    System.out.println(sysModuleEnum.getCode());
//                    System.out.println(sysModuleEnum.getName());
//                    System.out.println(sysModuleEnum.getDescription());
//                    System.out.println(sysModuleEnum.ordinal());
//                });
//
//        System.out.println("=========================== Stream.of() ==============================");
//        Stream.of(SysModuleEnum.values())
//                .forEach(sysModuleEnum -> {
//                    System.out.println(sysModuleEnum.name());
//                    System.out.println(sysModuleEnum.getCode());
//                    System.out.println(sysModuleEnum.getName());
//                    System.out.println(sysModuleEnum.getDescription());
//                    System.out.println(sysModuleEnum.ordinal());
//                });
//
//        System.out.println("=========================== 带过滤的遍历 ==============================");
//        Arrays.stream(SysModuleEnum.values())
//                .filter(sysModuleEnum -> StrUtil.startWith(sysModuleEnum.getCode(), "S"))
//                .forEach(sysModuleEnum -> {
//                    System.out.println(sysModuleEnum.name());
//                    System.out.println(sysModuleEnum.getCode());
//                    System.out.println(sysModuleEnum.getName());
//                    System.out.println(sysModuleEnum.getDescription());
//                    System.out.println(sysModuleEnum.ordinal());
//                });
//
//        System.out.println("=========================================================");
//    }

//    public static void main(String[] args) {
//
//        System.out.println("=========================================================");
//
//        int a0 = 0;
//        int a1 = 1;
//        int a20 = 20;
//        int a40 = 40;
//        int a50 = 50;
//        int a80 = 80;
//        int a99 = 99;
//        int a100 = 100;
//
//        // 用耗时算剩余分钟
//        long time = 3600000; // 1小时
//
//        pt(a0, time);
//        pt(a1, time);
//        pt(a20, time);
//        pt(a40, time);
//        pt(a50, time);
//        pt(a80, time);
//        pt(a99, time);
//        pt(a100, time);
//
//        System.out.println("=========================================================");
//    }
//
//    public static void pt(int a, long time) {
//        System.out.print("进度：" + a + "%    剩余时间（分钟）：");
//
//        long hm = time * (100 - a) / (a == 0 ? 1 : a);
//        long m = hm / 1000;
//        long f = m / 60;
//
//        System.out.println(f == 0 ? 1 : f);
//    }


    public static void main(String[] args) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        Date date1 = calendar.getTime();

        calendar.add(Calendar.MINUTE, -15);
        Date date2 = calendar.getTime();

        System.out.println("date1  ===  " + DateUtil.format(date1, "yyyy-MM-dd HH:mm:ss"));
        System.out.println("date2  ===  " + DateUtil.format(date2, "yyyy-MM-dd HH:mm:ss"));
    }








}
