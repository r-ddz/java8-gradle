package com.ddz.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import org.springframework.core.NamedThreadLocal;

import java.util.LinkedHashMap;

public class ThreadLocalUtil {

    /**
     * 限制ThreadLocal中的map容量，是为了防止内存泄露
     */
    private static final int MAX_SIZE = 100;

    // 没有会自动初始化， THREAD_LOCAL.get() 里的对象不需要判断为空则 new
    private static final ThreadLocal<LinkedHashMap<String, Object>> THREAD_LOCAL = ThreadLocal.withInitial(LinkedHashMap::new);
    // 重写了toString()方法，在调试时能显示"Name Local"，需要调试信息的复杂对象才会使用，
    private static final ThreadLocal THREAD_LOCAL_2 = new NamedThreadLocal("Name Local");
    // 最普通的线程变量，也最常用
    private static final ThreadLocal<String> THREAD_LOCAL_3 = new ThreadLocal<>();

    public static Object get(String key) {
        LinkedHashMap<String, Object> map = THREAD_LOCAL.get();
        if (MapUtil.isEmpty(map) || !map.containsKey(key)) {
            return null;
        }
        return map.get(key);
    }

    public static void remove(String key) {
        LinkedHashMap<String, Object> map = THREAD_LOCAL.get();
        if (MapUtil.isEmpty(map) || !map.containsKey(key)) {
            return;
        }
        map.remove(key);
    }

    public static void put(String key, Object value) {
        LinkedHashMap<String, Object> map = THREAD_LOCAL.get();
        // 这里防止线程池中使用ThreadLocal导致的内存泄露
        if (map.size() > MAX_SIZE) {
            if (CollUtil.isNotEmpty(map.keySet())) {
                String firstKey = map.keySet().iterator().next();
                map.remove(firstKey);
            }
        }
        THREAD_LOCAL.get().put(key, value);
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }

}
