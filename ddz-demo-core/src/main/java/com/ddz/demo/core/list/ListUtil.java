package com.ddz.demo.core.list;

import cn.hutool.core.collection.CollUtil;

import java.util.List;

public class ListUtil {

    /**
     * 分割集合    (使用 hutool 工具)
     *
     * @param list 集合
     * @param size 每个段的长度
     * @return
     * @param <T> 集合元素类型
     */
    public static <T> List<List<T>> split(List<T> list, int size) {
        return CollUtil.split(list, size);
    }


}
