package com.ddz.demo;

import cn.hutool.core.collection.CollUtil;
import com.ddz.demo.core.model.data.UserDemo;
import com.ddz.demo.core.model.data.UserDemoFactory;
import com.ddz.demo.core.model.info.UserInfo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MergeListTests {


    @Test
    public void test() {


        List<UserDemo> list1 = new ArrayList<>();
        list1.add(new UserDemo(1230001L, "code_1", "张三", null, "男", "正常", ""));
        list1.add(new UserDemo(1230002L, "code_2", "李四", 18, null, "正常", ""));
        list1.add(new UserDemo(1230003L, "code_3", "王五", 24, "男", "正常", ""));

        List<UserDemo> list2 = new ArrayList<>();
        list2.add(new UserDemo(1230001L, "code_1", "", null, null, null, "张三年龄不详"));
        list2.add(new UserDemo(1230002L, "code_2", "", null, null, null, "李四性别不详"));
        list2.add(new UserDemo(1230004L, "code_4", "新角色", null, null, null, "全部信息不详"));

        List<UserDemo> list = mergeAndMark(list1, list2);

        System.out.println("======================= 测试打印 ======================");
        for (UserDemo c : list) {
            System.out.println(c);
        }
        System.out.println("======================= 测试打印 ======================");
    }




    private List<UserDemo> mergeAndMark(List<UserDemo> list1, List<UserDemo> list2) {
        if (CollUtil.isEmpty(list2)) {
            return list1;
        }
        if (CollUtil.isEmpty(list1)) {
            return list2;
        }

        Function<UserDemo, String> keyExtractor = c -> c.getId() + ":" + c.getCode();
        Map<String, UserDemo> map1 = list1.stream()
                .collect(Collectors.toMap(keyExtractor, Function.identity(), (a, b) -> a));
        Map<String, UserDemo> map2 = list2.stream()
                .collect(Collectors.toMap(keyExtractor, Function.identity(), (a, b) -> a));

        List<UserDemo> result = new ArrayList<>();
        for (UserDemo c : list1) {
            String key = keyExtractor.apply(c);
            if (map2.containsKey(key)) {
                c.setRemark(map2.get(key).getRemark());
            }
            result.add(c);
        }
        for (UserDemo c : list2) {
            String key = keyExtractor.apply(c);
            if (!map1.containsKey(key)) {
                result.add(c);
            }
        }
        return result;
    }



}
