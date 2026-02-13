package com.ddz.demo.hutool;

import cn.hutool.core.util.StrUtil;

public class StrEqualsDemo {

    public static void main(String[] args) {
        System.out.println("================================= 测试开始 =================================");
        // 给定字符串是否与提供的中任一字符串相同，相同则返回true，没有相同的返回false
        equalsAny();
        System.out.println("================================= 测试结束 =================================");
    }

    /**
     * 给定字符串是否与提供的中任一字符串相同，相同则返回true，没有相同的返回false
     */
    public static void equalsAny(){

        String str = "AbcDeF";

        System.out.println("1 true  :  " + StrUtil.equalsAny(str, "", null, "AbcDeF"));
        System.out.println("2 true  :  " + StrUtil.equalsAny(str, "AbcDeF", "AbcDeF", "AbcDeF"));
        System.out.println("3 false :  " + StrUtil.equalsAny(str, "", null, "365", "8851"));
        System.out.println("4 true  :  " + StrUtil.equalsAny(null, "", null, "365", "8851"));
        System.out.println("5 true  :  " + StrUtil.equalsAny("", "", null, "365", "8851"));
        System.out.println("6 false :  " + StrUtil.equalsAny("      ", "", null, "365", "8851"));
        System.out.println("7 false :  " + StrUtil.equalsAny(str, "abcdef", "ABCDEF"));
        System.out.println("8 true  :  " + StrUtil.equalsAny(str, true, "abcdef", "ABCDEF"));
        System.out.println("9 false :  " + StrUtil.equalsAny(str, false, "abcdef", "ABCDEF"));
    }




}
