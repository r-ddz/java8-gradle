package com.ddz.demo.stream;

import java.util.*;
import java.util.stream.Collectors;

public class StreamDemo {

    public static void main(String[] args) {
        // 根据条件过滤数据，找到第一个满足条件的数据
        findFirst();
        // List => Map
        list2map();
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

    /**
     * List => Map
     */
    private static void list2map() {
        String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "2"};
        List<String> list = new ArrayList<>(Arrays.asList(arr));
//        Map<String, String> taskIdMap = webErpToDoEntity.stream()
//                .collect(Collectors.toMap(WebErpToDoEntity::getProcessInstanceId, WebErpToDoEntity::getTaskId, (k1, k2) -> k1));
        Map<String, String> map = list.stream()
                .collect(Collectors.toMap(
                        (String k) -> "key_" + k,
                        (String v) -> "value_" + v,
                        // 如果有重复的 key，则保留第一个 value
                        (k1, k2) -> k1
                ));
        System.out.println("================================= 开始 =================================");
        System.out.println(map);
        System.out.println("================================= 结束 =================================");
    }


}
