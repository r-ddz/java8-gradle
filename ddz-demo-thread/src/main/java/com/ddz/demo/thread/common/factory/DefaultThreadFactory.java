package com.ddz.demo.thread.common.factory;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义的默认线程工厂
 * 可以控制线程的创建方式、命名、优先级、守护状态等
 * YAML无法配置此功能
 *
 * @author zr
 */
@Slf4j
public class DefaultThreadFactory implements ThreadFactory {

    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private final int priority;
    private final Thread.UncaughtExceptionHandler exceptionHandler;

    public DefaultThreadFactory(String namePrefix) {
        this(namePrefix, Thread.NORM_PRIORITY);
    }

    public DefaultThreadFactory(String namePrefix, int priority) {
        this(namePrefix, priority, (t, e) -> {
            log.error("线程 {} 发生未捕获异常: {}", t.getName(), e.getMessage(), e);
            // 这里可以发送告警、记录监控指标等
        });
    }

    public DefaultThreadFactory(String namePrefix, int priority, Thread.UncaughtExceptionHandler exceptionHandler) {
        this.namePrefix = namePrefix;
        this.priority = priority;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(namePrefix + threadNumber.getAndIncrement());
        thread.setPriority(priority);
        thread.setDaemon(false);  // 设置为非守护线程

        // 设置未捕获异常处理器
        thread.setUncaughtExceptionHandler(exceptionHandler);

        return thread;
    }

}
