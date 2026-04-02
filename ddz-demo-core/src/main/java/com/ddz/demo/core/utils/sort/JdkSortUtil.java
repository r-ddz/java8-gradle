package com.ddz.demo.core.utils.sort;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * JDK 内置方法排序
 */
public class JdkSortUtil {

    /**
     * 取对象里的某个字段进行排序，字段的类型需要实现 Comparable 接口 拥有 compareTo 方法
     *
     * 如果有自定义的比较逻辑，可以在传入 keyExtractor 时，不直接返回对象的属性，而是作为权重计算传入，最后对权重排序即可
     *
     * @param list
     * @param keyExtractor
     * @param isAsc
     * @param isNullLast
     * @param <OBJ>
     * @param <T>
     */
    public static <OBJ, T extends Comparable<? super T>> void sort(List<OBJ> list,
            Function<? super OBJ, ? extends T> keyExtractor,
            boolean isAsc, boolean isNullLast)
    {
        // 根据 obj 对象的某个字段执行排序 【会改变原对象】
        list.sort(Comparator.comparing(
                keyExtractor,
                buildComparator(isAsc, isNullLast)
        ));
    }

    /**
     * 实现 Comparable 的对象通用排序工具
     * 利用 compareTo 方法进行排序
     * 常见的包装类 Integer、Long 等
     *
     * @param list
     * @param isAsc
     * @param isNullLast
     * @param <T>
     */
    public static <T extends Comparable<? super T>> void sort(List<T> list, boolean isAsc, boolean isNullLast) {
        // 执行排序 【会改变原对象】
        list.sort(buildComparator(isAsc, isNullLast));
    }

    /**
     * 构建排序比较器
     * @param isAsc 是否升序
     * @param isNullLast 是否将 null 放在最后
     * @return
     * @param <T>
     */
    public static <T extends Comparable<? super T>> Comparator<T> buildComparator(boolean isAsc, boolean isNullLast) {
        // 自然升序 or 自然降序
        Comparator<T> comparator = isAsc ? Comparator.naturalOrder() : Comparator.reverseOrder();
        // null放在最后 or null放在最前 【如果存在null值但是没声明 nullComparator，会在内部调用compareTo的时候抛异常】
        return isNullLast ? Comparator.nullsLast(comparator) : Comparator.nullsFirst(comparator);
    }

}
