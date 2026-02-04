package com.ddz.demo.test;


import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Lambda 表达式
 *
 * @author admin
 * @version 1.0
 * @date 2023/04/26
 */
public class LambdaDemo {

    //region 开始
    // TODO 数据库操作
    //endregion 结束

    /*
    特征：
        1. Lambda表达式，可以简单理解为是匿名内部类的简写。
        2. 方法的参数或局部变量类型必须为接口才能使用Lambda。
        3. 接口中有且仅有一个抽象方法，建议接口上加 @FunctionalInterface 注解来确保这是一个函数式接口。

    原理：
        1. 在程序运行的时候会生成一个匿名内部类的 class 文件，Lambda 表达式实际上调用的就是这个匿名内部类。
        2. 这个匿名内部类就是实现了接口的唯一方法，并且在当前执行类里生成了一个匿名的静态方法。
        3. Lambda表达式里的代码逻辑就全在这里了，这个匿名的静态方法会被匿名内部类所调用。
        4. 而正常的匿名内部类则是在编译的时候就生成了 class 文件，这也是两者的区别。

    标准格式：
        (参数类型 参数名称) -> {
            代码体;
        }

    省略格式：
        1. 小括号内参数的类型可以省略。
        2. 如果小括号内有且仅有一个参数，则小括号可以省略。
        3. 如果大括号内有且仅有一个语句，可以同时省略大括号、return关键字及语句分号。
     */


    /**
     * Lambda表达式与匿名内部类的写法对比
     */
    static void demo1() {
        // 匿名内部类
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("匿名内部类线程执行");
            }
        }).start();
        // Lambda表达式
        new Thread(() -> System.out.println("Lambda表达式线程执行")).start();
    }

    /**
     * 常用的内置函数式接口: Supplier(供给型接口，通过 Supplier 接口中的 get 方法可以得到一个值，无参有返回值的接口)
     * 利用内置函数式接口 Supplier 返回数组元素最大值
     */
    static void demo2() {
        int[] arr1 = {1, 2, 3, 4, 5, 998};
        int[] arr2 = {1, 2, 3, 4, 5, 99};
        // 匿名内部类
        testSupplier(new Supplier<Integer>() {
            @Override
            public Integer get() {
//                int[] arr = {1, 2, 3, 4, 5, 998};
                Arrays.sort(arr1); // 升序排序
                return arr1[arr1.length - 1];
            }
        });
        // 使用 Lambda 表达式
        testSupplier(() -> {
//            int[] arr = {1, 2, 3, 4, 5, 99};
            Arrays.sort(arr2); // 升序排序
            return arr2[arr2.length - 1];
        });
    }

    static void testSupplier(Supplier<Integer> supplier) {
        int max = supplier.get();
        System.out.println("max = " + max);
    }

    /**
     * 常用的内置函数式接口: Consumer(消费型接口，可以拿到 accept 方法参数传递过来的数据进行处理，有参无返回值的接口)
     * 利用内置函数式接口 Consumer 将一个字符串转大写，并输出
     */
    static void demo3() {
        // 匿名内部类
        testConsumer("Hello World", new Consumer<String>() {
            @Override
            public void accept(String str) {
                System.out.println(str.toUpperCase());
            }
        });
        // 使用 Lambda 表达式
        testConsumer("Welcome to China", str -> System.out.println(str.toUpperCase()));
    }

    static void testConsumer(String str, Consumer<String> consumer) {
        consumer.accept(str);
    }

    /**
     * 常用的内置函数式接口: Function<T, R>(转换型接口，对 apply 方法传入的 T 类型数据进行处理，返回 R 类型的结果，有参有返回值的接口)
     * 利用内置函数式接口 Function<T, R> 将一个将字符串转数字，并输出
     */
    static void demo4() {
        // 匿名内部类
        testFunction("10", new Function<String, Integer>(){
            @Override
            public Integer apply(String str) {
                return Integer.parseInt(str);
            }
        });
        // 使用 Lambda 表达式
        testFunction("13", str -> Integer.parseInt(str));
    }

    static void testFunction(String str, Function<String, Integer> function) {
        Integer num = function.apply(str);
        System.out.println("num = " + num);
    }

    /**
     * 常用的内置函数式接口: Predicate<T>(判断型接口，对 test 方法的参数 T 进行判断，返回 boolean 类型的结果，用于条件判断的场景)
     * 利用内置函数式接口 Predicate<T> 判断字符串长度是否 > 3
     */
    static void demo5() {
        // 匿名内部类
        testPredicate("ABCD", new Predicate<String>(){
            @Override
            public boolean test(String str) {
                return str.length() > 3;
            }
        });
        // 使用 Lambda 表达式
        testPredicate("AB", str -> str.length() > 3);
    }

    public static void testPredicate(String str, Predicate<String> predicate) {
        boolean isLong = predicate.test(str);
        System.out.println("字符串长度是否 > 3 ： " + isLong);
    }

}
