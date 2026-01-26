package com.ddz.demo.thread.common.policy;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 拒绝策略工厂 - YAML无法配置拒绝策略，必须通过代码设置
 *
 * 这里用于管理拒绝策略的单例，实现复用，而不需要每一个策略都靠new创建
 *
 * ThreadPoolExecutor提供了4种内置策略：
 *      - AbortPolicy（默认）：抛出RejectedExecutionException
 *      - CallerRunsPolicy：由调用线程执行任务
 *      - DiscardPolicy：直接丢弃任务
 *      - DiscardOldestPolicy：丢弃队列中最老的任务
 *      - 也可以自定义拒绝策略
 *
 * @author zr
 */
public class RejectPolicyFactory {

    /**
     * 默认拒绝策略 - 抛出RejectedExecutionException
     *
     * @return 默认拒绝策略
     */
    public static RejectedExecutionHandler defaultPolicy() {
        return abortPolicy();
    }

    /**
     * 拒绝策略 - 抛出RejectedExecutionException
     *
     * @return 拒绝策略
     */
    public static RejectedExecutionHandler abortPolicy() {
        return AbortPolicyHolder.INSTANCE;
    }

    /**
     * 拒绝策略 - 由调用线程执行任务
     *
     * @return 拒绝策略
     */
    public static RejectedExecutionHandler callerRunsPolicy() {
        return CallerRunsPolicyHolder.INSTANCE;
    }

    /**
     * 拒绝策略 - 直接丢弃任务
     *
     * @return 拒绝策略
     */
    public static RejectedExecutionHandler discardPolicy() {
        return DiscardPolicyHolder.INSTANCE;
    }

    /**
     * 拒绝策略 - 丢弃队列中最老的任务
     *
     * @return 拒绝策略
     */
    public static RejectedExecutionHandler discardOldestPolicy() {
        return DiscardOldestPolicyHolder.INSTANCE;
    }

    /**
     * AbortPolicy（默认）：抛出RejectedExecutionException
     *
     * 静态内部类实现单例模式
     * 利用 JVM 类加载机制，本身就是线程安全的，直接访问静态变量，无任何同步开销，更快
     */
    private static class AbortPolicyHolder {
        static final RejectedExecutionHandler INSTANCE = new ThreadPoolExecutor.AbortPolicy();
    }

    /**
     * CallerRunsPolicy：由调用线程执行任务
     *
     * 静态内部类实现单例模式
     * 利用 JVM 类加载机制，本身就是线程安全的，直接访问静态变量，无任何同步开销，更快
     */
    private static class CallerRunsPolicyHolder {
        static final RejectedExecutionHandler INSTANCE = new ThreadPoolExecutor.CallerRunsPolicy();
    }

    /**
     * DiscardPolicy：直接丢弃任务
     *
     * 静态内部类实现单例模式
     * 利用 JVM 类加载机制，本身就是线程安全的，直接访问静态变量，无任何同步开销，更快
     */
    private static class DiscardPolicyHolder {
        static final RejectedExecutionHandler INSTANCE = new ThreadPoolExecutor.DiscardPolicy();
    }

    /**
     * DiscardOldestPolicy：丢弃队列中最老的任务
     *
     * 静态内部类实现单例模式
     * 利用 JVM 类加载机制，本身就是线程安全的，直接访问静态变量，无任何同步开销，更快
     */
    private static class DiscardOldestPolicyHolder {
        static final RejectedExecutionHandler INSTANCE = new ThreadPoolExecutor.DiscardOldestPolicy();
    }

    private RejectPolicyFactory() {
        // 私有构造器，防止实例化
    }

}
