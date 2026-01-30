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
 * 考虑性能优先，选用 ConcurrentLinkedDeque 无锁（CAS）队列，它的添加和移除是线程安全的
 * ConcurrentLinkedDeque.size() 是遍历实现，性能低，并且在并发场景下存在误差
 * new AtomicInteger(0) 我们加入一个自己的计数器，免得调用 size()
 *
 * 因为这里只是预警、日志等行为，可以接收少量的误差，因此不为少量误差而做加锁行为影响性能
 */
@Slf4j
public class MonitoringCallerRunsPolicy extends ThreadPoolExecutor.CallerRunsPolicy {

    /** 常量定义 - 日志打印间隔时间(毫秒) */
    private static final long LOG_INTERVAL_MILLI = 1000;
    /** 常量定义 - 默认的触发预警的次数 maxCount */
    private static final int DEFAULT_MAX_COUNT = 100;
    /** 常量定义 - 默认的阈值，时间窗口，触发预警的间隔时间（毫秒），短时间达到触发预警的次数就需要预警 */
    private static final long DEFAULT_INTERVAL_MILLI = 5000;

    /** volatile 线程共享读取 - 最后一次打印日志时间，可用于防日志风暴 */
    private volatile long lastWarnTime = 0;
    /** volatile 线程共享读取 - 最后一次打印严重预警日志时间，可用于防日志风暴 */
    private volatile long lastSevereWarnTime = 0;

    /** 线程池名称 */
    private final String threadPoolName;
    /** 是否启用计数预警 */
    private final boolean countWarnEnable;
    /** 触发预警的次数 （逆向思维的体现：固定的时间窗口实现复杂，使用“固定次数、动态时间窗口”来计算频率） */
    private final int maxCount;
    /** 阈值，时间窗口，触发预警的间隔时间（毫秒），短时间达到触发预警的次数就需要预警 */
    private final long intervalMilli;

    /** 时间窗口队列 */
    private final ConcurrentLinkedDeque<Long> deque = new ConcurrentLinkedDeque<>();
    /** 计数器 */
    private final AtomicInteger counter = new AtomicInteger(0);

    public MonitoringCallerRunsPolicy(String threadPoolName) {
        this(threadPoolName, false);
    }

    public MonitoringCallerRunsPolicy(String threadPoolName, boolean countWarnEnable) {
        this(threadPoolName, countWarnEnable, DEFAULT_MAX_COUNT, DEFAULT_INTERVAL_MILLI);
    }

    public MonitoringCallerRunsPolicy(String threadPoolName, boolean countWarnEnable, int maxCount, long intervalMilli) {
        this.threadPoolName = threadPoolName;
        this.countWarnEnable = countWarnEnable;
        this.maxCount = maxCount;
        this.intervalMilli = intervalMilli;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

        if (countWarnEnable) {
            // 启用计数预警
            countWarn(executor);
        } else {
            // 不启用计数预警，只记录日志，也不防日志风暴
            warnLog(executor);
        }

        // 仍然执行父类的CallerRunsPolicy策略（由调用者线程执行）
        super.rejectedExecution(r, executor);
    }

    private void countWarn(ThreadPoolExecutor executor) {
        long now = System.currentTimeMillis();
        // 优化点1：【状态先行理念】 对于一定成功的 deque.offer(now) 操作，先增加计数器，再放入时间戳。
        int count = counter.incrementAndGet();
        deque.offer(now);
        // 检查预警，严重警告和普通警告不需要重叠记录
        boolean isWarn = false;
        if (count > maxCount) {
            // 优化点2：清理事件独立，与计算行为分开，代码可读性高
            Long oldest = deque.peek();
            if (oldest != null && (now - oldest) < intervalMilli) {
                // 简单检查：距离上次打印是否超过1秒（允许少量边界误差） 这里只是防止日志风暴
                if (now - lastSevereWarnTime >= LOG_INTERVAL_MILLI) {
                    lastSevereWarnTime = now; // 直接赋值，非原子，但没关系
                    // 时间周期内，超过了触发次数，执行日志预警
                    severeWarnLog(executor, count, now - oldest);
                    isWarn = true;
                }
            }
            // 无论是否警告，都尝试清理以维持队列稳定
            clearDeque(5);
        }
        if (!isWarn) {
            if (now - lastWarnTime >= LOG_INTERVAL_MILLI) {
                lastWarnTime = now; // 直接赋值，非原子，但没关系
                // 普通警告日志一次
                warnLog(executor);
            }
        }
    }

    private void clearDeque(int num) {
        if (num > 0 && counter.get() > maxCount) {
            Long oldest = deque.poll();
            if (oldest != null && counter.decrementAndGet() > maxCount) {
                clearDeque(--num);
            }
        }
    }

    private void warnLog(ThreadPoolExecutor executor) {
        log.warn("【线程池预警 - 警告】线程池[{}]触发拒绝策略，当前状态: 活跃线程数={}, 队列大小={}, 最大线程数={}",
                threadPoolName,
                executor.getActiveCount(),
                executor.getQueue().size(),
                executor.getMaximumPoolSize());
    }

    private void severeWarnLog(ThreadPoolExecutor executor, int count, long gapTime) {
        log.warn("【线程池预警 - 严重警告】线程池[{}]触发拒绝策略,集中在{}毫秒内触发了{}次，，当前状态: 活跃线程数={}, 队列大小={}, 最大线程数={}",
                threadPoolName,
                gapTime,
                count,
                executor.getActiveCount(),
                executor.getQueue().size(),
                executor.getMaximumPoolSize());
    }

}