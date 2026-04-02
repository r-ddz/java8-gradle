package com.ddz.demo.core.utils.sort;

import com.ddz.demo.core.model.data.UserDemo;
import com.ddz.demo.core.model.data.UserDemoFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class JdkSortUtilTests {

    @Test
    public void sort1() {
        List<UserDemo> list = UserDemoFactory.buildUser(5);
        // 特意添加一个null
        list.get(0).setAge(null);

        JdkSortUtil.sort(list, UserDemo::getAge, true, true);
        System.out.println(list);

        JdkSortUtil.sort(list, UserDemo::getAge, true, false);
        System.out.println(list);

        JdkSortUtil.sort(list, UserDemo::getAge, false, true);
        System.out.println(list);

        JdkSortUtil.sort(list, UserDemo::getAge, false, false);
        System.out.println(list);
    }



    @Test
    public void sort() {
        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(2);
        list.add(7);
        list.add(4);
        list.add(null);

        JdkSortUtil.sort(list, true, true);
        System.out.println(list); // [2, 4, 5, 7, null]

        JdkSortUtil.sort(list, true, false);
        System.out.println(list); // [null, 2, 4, 5, 7]

        JdkSortUtil.sort(list, false, true);
        System.out.println(list); // [7, 5, 4, 2, null]

        JdkSortUtil.sort(list, false, false);
        System.out.println(list); // [null, 7, 5, 4, 2]
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
