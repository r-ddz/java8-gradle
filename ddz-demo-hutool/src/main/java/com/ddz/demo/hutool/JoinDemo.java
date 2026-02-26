package com.ddz.demo.hutool;

import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.List;

public class JoinDemo {


    public static void main(String[] args) {
        System.out.println("================================= 测试开始 =================================");
        // 截取字符串，从前面开始截取
        join2();
        System.out.println("================================= 测试结束 =================================");
    }

    /**
     * 截取字符串，从前面开始截取
     */
    public static void join(){
        System.out.println(StrUtil.join(",", "A"));
        System.out.println(StrUtil.join(",", "A", "B"));
        System.out.println(StrUtil.join(",", "A", "A", "B"));

        String[] arr = {"A", "B", "C"};
        System.out.println(StrUtil.join(",", (Object) arr));


        System.out.println(String.join(",", arr));
        System.out.println(String.join(",", "A"));
        System.out.println(String.join(",", "A", "B"));
        System.out.println(String.join(",", "A", "A", "B"));

    }

    public static void join2(){
        List<String> list = Arrays.asList("A11111", "B22222", "C33333");


        System.out.println(String.join(",", list));
        System.out.println(String.join("\n", list));

    }



}
