package com.ddz.demo.thread.common.policy;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义拒绝策略 - 演示如何实现自定义拒绝逻辑
 * 可以记录指标、发送告警、持久化任务等
 *
 * @author ddz
 */
@Slf4j
public class CustomRejectionPolicy extends ThreadPoolExecutor.AbortPolicy {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        // 记录被拒绝的任务
        log.warn("任务被线程池拒绝: 活跃线程数={}, 队列大小={}, 最大线程数={}",
                executor.getActiveCount(),
                executor.getQueue().size(),
                executor.getMaximumPoolSize());

        // 发送监控告警
        sendAlertToMonitor(executor);

        // 尝试将任务持久化，稍后重试
        persistTaskForRetry(r);

        // 调用父类默认行为（抛出异常）
        super.rejectedExecution(r, executor);
    }

    private void sendAlertToMonitor(ThreadPoolExecutor executor) {
        // 实现告警逻辑
        log.error("线程池达到极限，需要扩容或优化！");
    }

    private void persistTaskForRetry(Runnable r) {
        // 实现任务持久化逻辑
        // 可以将任务存入数据库或消息队列，稍后重试
    }
}
