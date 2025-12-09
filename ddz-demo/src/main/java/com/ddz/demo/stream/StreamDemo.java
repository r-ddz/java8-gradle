package com.ddz.demo.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StreamDemo {

    public static void main(String[] args) {
        // 根据条件过滤数据，找到第一个满足条件的数据
        findFirst();
    }

    /**
     * 根据条件过滤数据
     * 找到第一个满足条件的数据
     */
    private static void findFirst() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        // 使用流，根据条件过滤数据，找到第一个满足条件的数据
        Optional<Integer> optional = list.stream()
                .filter(i -> i == 5)
                .findFirst();

        if (optional.isPresent()) {
            System.out.println("找到数据： " + optional.get());
        } else {
            System.out.println("没有找到");
        }
    }


}
