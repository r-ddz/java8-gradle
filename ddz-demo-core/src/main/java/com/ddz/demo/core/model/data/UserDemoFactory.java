package com.ddz.demo.core.model.data;

import cn.hutool.core.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class UserDemoFactory {

    public static final String SEX_MAN = "男";
    public static final String SEX_WOMAN = "女";

    public static final String STATUS_NORMAL = "正常";
    public static final String STATUS_LOCK = "锁定";

    public static UserDemo buildUser() {
        return new UserDemo(1230001L, "code_1", "张三", 18, SEX_MAN, STATUS_NORMAL, "测试备注");
    }

    public static List<UserDemo> buildUser(int size) {
        List<UserDemo> users = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            users.add(new UserDemo(1230000L+i, "code_"+i, randomName(), randomAge(), randomSex(), randomStatus(), "测试备注"+i));
        }
        return users;
    }

    //随机年龄方法
    private static int randomAge(){
        return RandomUtil.randomInt(1, 120);
    }

    //随机性别
    private static String randomSex(){
        return RandomUtil.randomBoolean() ? SEX_MAN : SEX_WOMAN;
    }

    //随机状态
    private static String randomStatus(){
        return RandomUtil.randomBoolean() ? STATUS_NORMAL : STATUS_LOCK;
    }

    // 生成随机名字
    private static String randomName(){
        return RandomUtil.randomEle(new String[]{"张", "王", "李", "赵", "孙", "李", "周", "吴", "郑", "王"}) +
                RandomUtil.randomEle(new String[]{"三", "四", "五", "六", "七", "八", "九", "十一", "十二", "十三", "十四", "十五", "十六", "十七"});
    }

}

