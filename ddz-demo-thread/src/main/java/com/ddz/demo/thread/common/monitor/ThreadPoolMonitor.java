package com.ddz.demo.thread.common.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
//@Component
public class ThreadPoolMonitor {

//    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

//    @Autowired(required = false)
    @Qualifier("fastExecutor")
    private ThreadPoolTaskExecutor fastExecutor;

//    @Autowired(required = false)
    @Qualifier("ioExecutor")
    private ThreadPoolTaskExecutor ioExecutor;

    @Scheduled(fixedRate = 10000) // 每10秒收集一次指标
    public void collectMetrics() {
        collectExecutorMetrics("default", taskExecutor);
        if (fastExecutor != null) collectExecutorMetrics("fast", fastExecutor);
        if (ioExecutor != null) collectExecutorMetrics("io", ioExecutor);
    }

    private void collectExecutorMetrics(String poolName, ThreadPoolTaskExecutor executor) {
        ThreadPoolExecutor threadPool = executor.getThreadPoolExecutor();

        // 收集关键指标
        int activeCount = threadPool.getActiveCount();
        int poolSize = threadPool.getPoolSize();
        int queueSize = threadPool.getQueue().size();
        long completedTaskCount = threadPool.getCompletedTaskCount();

        // 记录到日志
        log.debug("线程池[{}]监控 - 活跃: {}/{}, 队列: {}, 完成任务: {}",
                poolName, activeCount, poolSize, queueSize, completedTaskCount);

        // 发送到监控系统（如Prometheus）
//        Metrics.gauge("threadpool.active.count", poolName).set(activeCount);
//        Metrics.gauge("threadpool.queue.size", poolName).set(queueSize);
//        Metrics.gauge("threadpool.pool.size", poolName).set(poolSize);

        // 检查告警条件
        if (queueSize > executor.getQueueCapacity() * 0.8) {
            log.warn("线程池[{}]队列使用率超过80%: {}/{}",
                    poolName, queueSize, executor.getQueueCapacity());
        }

        if (activeCount == executor.getMaxPoolSize()) {
            log.error("线程池[{}]线程已耗尽，需要扩容!", poolName);
        }
    }

}
