package com.ddz.demo.stream;

import java.util.*;
import java.util.stream.Collectors;

public class StreamDemo {

    public static void main(String[] args) {
        System.out.println("================================= 开始 =================================");

        // List => Map
        list2map();
        // 对象转换 List<obj1> => List<obj2>
        list2list();

        // 找到所有满足条件的数据
        findAll();
        // 根据条件过滤数据，找到第一个满足条件的数据
        findFirst();

        // 排序
        sorted();
        // 获取最大值、最小值
        getMaxAndMin();

        // 是否存在
        isExist();

        System.out.println("================================= 结束 =================================");
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

    /**
     * 对象转换
     */
    private static void list2list() {
        int[] arr = {5, 2, 7, 4, 1, 6, 3, 3, 9, 6};
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        // 对象转换
        List<Map<String, Object>> mapList = list.stream()
                .map(i -> {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("id" + i, i);
                    return itemMap;
                })
                .collect(Collectors.toList());
        System.out.println("对象转换： " + mapList);
    }

    /**
     * 找到所有满足条件的数据
     */
    private static void findAll() {
        int[] arr = {5, 2, 7, 4, 1, 6, 3, 3, 9, 6};
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        // 收集大于5的
        List<Integer> collect = list.stream()
                .filter(i -> i > 5)
                .collect(Collectors.toList());
        System.out.println("收集大于5的： " + collect);
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
     * 排序
     */
    private static void sorted() {
        int[] arr = {5, 2, 7, 4, 1, 6, 3, 8, 9, 10};
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        // 倒序排序存新的list
        List<Integer> list1 = list.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        System.out.println("倒序排序： " + list1);

        // 正序排序存新的list
        List<Integer> list2 = list.stream()
                .sorted(Comparator.comparingInt(Integer::intValue))
                .collect(Collectors.toList());
        System.out.println("正序排序： " + list2);
    }

    /**
     * 获取最大值、最小值
     */
    private static void getMaxAndMin() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        // 获取最大值
        Optional<Integer> optional = list.stream()
                .max(Comparator.comparingInt(Integer::intValue));
        if (optional.isPresent()) {
            System.out.println("找到数据max： " + optional.get());
        } else {
            System.out.println("没有找到max");
        }
        // 获取最小值
        optional = list.stream()
                .min(Comparator.comparingInt(Integer::intValue));
        if (optional.isPresent()) {
            System.out.println("找到数据min： " + optional.get());
        } else {
            System.out.println("没有找到min");
        }
    }

    /**
     * 是否存在
     */
    private static void isExist() {
        int[] arr = {5, 2, 7, 4, 1, 6, 3, 3, 9, 6};
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        // 是否存在值：4
        boolean isExist = list.stream()
                .anyMatch(i -> i == 4);
        System.out.println("是否存在值：4： " + isExist);
        // 是否不存在值：6
        boolean isNotExist = list.stream()
                .noneMatch(i -> i == 6);
        System.out.println("是否没有值：6： " + isNotExist);
        // 是否全部大于0
        boolean allMatch = list.stream()
                .allMatch(i -> i > 0);
        System.out.println("是否全部大于0： " + allMatch);
    }

}
