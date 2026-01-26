package com.ddz.demo.thread.core.pooltask.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 简单线程池配置类，直接从yaml读取配置
 *
 * @author ddz
 */
@Slf4j
//@Configuration
public class EasyThreadPoolTaskConfig {

    // ==================== 从配置文件读取参数（可选）====================
    // 即使使用配置类，也可以从application.yml读取部分参数
    @Value("${spring.task.execution.pool.core-size:8}")
    private int corePoolSize;

    @Value("${spring.task.execution.pool.max-size:50}")
    private int maxPoolSize;

    @Value("${spring.task.execution.pool.queue-capacity:1000}")
    private int queueCapacity;

    @Value("${spring.task.execution.pool.keep-alive:60}")
    private int keepAliveSeconds;

    @Value("${spring.task.execution.thread-name-prefix:easy-task-}")
    private String threadNamePrefix;

    @Value("${spring.task.execution.pool.allow-core-thread-timeout:false}")
    private boolean allowCoreThreadTimeOut;


    @Bean("easyExecutor") // 明确命名
    public Executor easyExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // ========== 基本配置（对应YAML中的配置）==========
        // 1. 核心线程数 - 线程池维护的最小线程数
        //    对应YAML: spring.task.execution.pool.core-size
        executor.setCorePoolSize(corePoolSize);

        // 2. 最大线程数 - 当队列满时，最多可以创建的线程数
        //    对应YAML: spring.task.execution.pool.max-size
        executor.setMaxPoolSize(maxPoolSize);

        // 3. 队列容量 - 任务队列的大小
        //    对应YAML: spring.task.execution.pool.queue-capacity
        executor.setQueueCapacity(queueCapacity);

        // 4. 线程名前缀 - 便于日志追踪和监控
        //    对应YAML: spring.task.execution.thread-name-prefix
        executor.setThreadNamePrefix(threadNamePrefix);

        // 5. 线程空闲时间 - 非核心线程空闲多久后被回收
        //    对应YAML: spring.task.execution.pool.keep-alive
        executor.setKeepAliveSeconds(keepAliveSeconds);

        // 6. 是否允许核心线程超时 - 核心线程空闲时是否可以被回收
        //    对应YAML: spring.task.execution.pool.allow-core-thread-timeout
        executor.setAllowCoreThreadTimeOut(allowCoreThreadTimeOut);

        executor.initialize();

        log.info("ThreadPoolTaskExecutor 初始化完成: core={}, max={}, queue={}, prefix={}",
                corePoolSize, maxPoolSize, queueCapacity, threadNamePrefix);

        return executor;
    }



}
