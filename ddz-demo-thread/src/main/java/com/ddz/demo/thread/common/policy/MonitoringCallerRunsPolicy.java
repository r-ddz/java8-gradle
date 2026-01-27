package com.ddz.demo.thread.common.policy;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义拒绝策略：记录日志、预警并执行CallerRunsPolicy
 *
 * 实现简易计数器，时间周期内，超过了触发次数，进行严重警告
 *
 * 考虑性能优先，选用 ConcurrentLinkedDeque 无锁队列，它的添加和移除是线程安全的
 * ConcurrentLinkedDeque.size() 是遍历实现，性能低，并且在并发场景下存在误差
 * new AtomicInteger(0) 我们加入一个自己的计数器，免得调用 size()
 *
 * 因为这里只是预警、日志等行为，可以接收少量的误差，因此不为少量误差而做加锁行为影响性能
 */
@Slf4j
public class MonitoringCallerRunsPolicy extends ThreadPoolExecutor.CallerRunsPolicy {

    private String threadPoolName;

    private int maxCount = 100;

    private long intervalMilli = 60 * 1000;

    private final ConcurrentLinkedDeque<Long> deque = new ConcurrentLinkedDeque<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    public MonitoringCallerRunsPolicy(String threadPoolName) {
        this.threadPoolName = threadPoolName;
    }

    public MonitoringCallerRunsPolicy(String threadPoolName, int maxCount, long intervalMilli) {
        this.threadPoolName = threadPoolName;
        this.maxCount = maxCount;
        this.intervalMilli = intervalMilli;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        long now = System.currentTimeMillis();
        deque.offer(now);
        // 检查预警
        if (counter.incrementAndGet() > maxCount) {
            Long oldest = deque.poll();
            if (oldest != null) {
                int count = counter.decrementAndGet();
                if (now - oldest < intervalMilli) {
                    // 时间周期内，超过了触发次数，执行日志预警
                    log.warn("【线程池预警 - 严重警告】线程池[{}]在{}毫秒内触发{}次拒绝策略，当前状态: 活跃线程数={}, 队列大小={}, 最大线程数={}",
                            threadPoolName,
                            now - oldest,
                            count,
                            executor.getActiveCount(),
                            executor.getQueue().size(),
                            executor.getMaximumPoolSize());
                }
                // 尽可能保持队列长度，同时防止处理时间过长，限制次数的尝试清理
                clear(5);
            }
        }

        log.warn("【线程池预警 - 警告】线程池[{}]触发拒绝策略，当前状态: 活跃线程数={}, 队列大小={}, 最大线程数={}",
                threadPoolName,
                executor.getActiveCount(),
                executor.getQueue().size(),
                executor.getMaximumPoolSize());

        // 仍然执行父类的CallerRunsPolicy策略（由调用者线程执行）
        super.rejectedExecution(r, executor);
    }

    private void clear(int num) {
        if (num > 0 && counter.get() > maxCount) {
            Long oldest = deque.poll();
            if (oldest != null && counter.decrementAndGet() > maxCount) {
                clear(--num);
            }
        }
    }

}