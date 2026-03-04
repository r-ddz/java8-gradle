package com.ddz.demo.hutool;

import cn.hutool.core.util.StrUtil;

public class StrSubDemo {


    public static void main(String[] args) {
        System.out.println("================================= 测试开始 =================================");
        // 截取字符串，从前面开始截取
//        subPre();

        remove();
        System.out.println("================================= 测试结束 =================================");
    }

    /**
     * 截取字符串，从前面开始截取
     */
    public static void subPre(){
        System.out.println(StrUtil.subPre("", 5));
        System.out.println(StrUtil.subPre(null, 5));
        System.out.println(StrUtil.subPre("123456789", 5));
        System.out.println(StrUtil.subPre("123", 5));
    }


    public static void remove(){
        String a1 = "http://api.example.com/a/";
        String a2 = "http://api.example.com/a//";
        String a3 = "http://api.example.com/a";
        System.out.println(StrUtil.removeSuffix(a1, "/"));
        System.out.println(StrUtil.removeSuffix(a2, "/"));
        System.out.println(StrUtil.removeSuffix(a3, "/"));

        String b1 = "/b/list";
        String b2 = "//b/list";
        String b3 = "b/list";
        System.out.println(StrUtil.removePrefix(b1, "/"));
        System.out.println(StrUtil.removePrefix(b2, "/"));
        System.out.println(StrUtil.removePrefix(b3, "/"));

    }



}
