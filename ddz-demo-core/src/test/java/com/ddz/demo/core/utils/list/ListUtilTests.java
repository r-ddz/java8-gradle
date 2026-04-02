package com.ddz.demo.core.utils.list;

import com.ddz.demo.core.list.ListUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ListUtilTests {

    @Test
    public void split() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        List<List<String>> ll = ListUtil.split(list, 4);
        System.out.println(ll); // [[1, 2, 3, 4], [5, 6, 7, 8], [9, 10]]
    }

    @BeforeAll
    public static void beforeAll() {
        System.out.println("================================= 测试开始 =================================");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("================================= 测试结束 =================================");
    }
}
