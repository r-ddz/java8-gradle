package com.ddz.demo.hutool;

import cn.hutool.core.util.StrUtil;

import java.util.Arrays;

public class StrFormatDemo {

    public static void main(String[] args) {
        System.out.println("================================= 测试开始 =================================");
        // 格式化字符串
        format();
        System.out.println("================================= 测试结束 =================================");
    }

    /**
     * 格式化字符串
     */
    public static void format(){
        System.out.println(StrUtil.format("测试1：{} ^^^ {}", 12345, "ABC"));
        System.out.println(StrUtil.format("测试3：{}", Arrays.asList("aaaa","bbbb")));

    }


}
