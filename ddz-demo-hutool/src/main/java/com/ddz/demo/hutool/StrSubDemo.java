package com.ddz.demo.hutool;

import cn.hutool.core.util.StrUtil;

public class StrSubDemo {


    public static void main(String[] args) {
        System.out.println("================================= 测试开始 =================================");
        // 截取字符串，从前面开始截取
        subPre();
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




}
