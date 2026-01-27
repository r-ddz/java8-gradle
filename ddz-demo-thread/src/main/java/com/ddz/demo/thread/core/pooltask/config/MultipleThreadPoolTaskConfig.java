package com.ddz.demo.thread.core.pooltask.config;

import com.ddz.demo.thread.common.policy.MonitoringCallerRunsPolicy;
import com.ddz.demo.thread.common.policy.RejectPolicyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@Configuration
public class MultipleThreadPoolTaskConfig {

    @Value("${spring.task.execution.pools.defTask.core-size:8}")
    private int defTaskCorePoolSize;
    @Value("${spring.task.execution.pools.defTask.max-size:32}")
    private int defTaskMaxPoolSize;
    @Value("${spring.task.execution.pools.defTask.queue-capacity:128}")
    private int defTaskQueueCapacity;
    @Value("${spring.task.execution.pools.defTask.thread-name-prefix:def-task-}")
    private String defTaskThreadNamePrefix;
    @Value("${spring.task.execution.pools.defTask.keep-alive:60}")
    private int defTaskKeepAliveSeconds;
    @Value("${spring.task.execution.pools.defTask.allow-core-thread-timeout:false}")
    private boolean defTaskAllowCoreThreadTimeOut;

    /**
     * 默认线程池 - 池子大，快速任务，快速响应，保活时间短，但不回收核心线程
     *
     * @return 线程池
     */
    @Primary
    @Bean("defTask")
    public Executor defTask() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(defTaskCorePoolSize);
        executor.setMaxPoolSize(defTaskMaxPoolSize);
        executor.setQueueCapacity(defTaskQueueCapacity);
        executor.setThreadNamePrefix(defTaskThreadNamePrefix);
        executor.setKeepAliveSeconds(defTaskKeepAliveSeconds);
        executor.setAllowCoreThreadTimeOut(defTaskAllowCoreThreadTimeOut);
        executor.setRejectedExecutionHandler(RejectPolicyFactory.callerRunsPolicy());
        executor.initialize();
        log.info("ThreadPoolTaskExecutor 【defTask】 初始化完成: core={}, max={}, queue={}, prefix={}",
                defTaskCorePoolSize, defTaskMaxPoolSize, defTaskQueueCapacity, defTaskThreadNamePrefix);
        return executor;
    }



    @Value("${spring.task.execution.pools.bigTask.core-size:4}")
    private int bigTaskCorePoolSize;
    @Value("${spring.task.execution.pools.bigTask.max-size:8}")
    private int bigTaskMaxPoolSize;
    @Value("${spring.task.execution.pools.bigTask.queue-capacity:32}")
    private int bigTaskQueueCapacity;
    @Value("${spring.task.execution.pools.bigTask.thread-name-prefix:big-task-}")
    private String bigTaskThreadNamePrefix;
    @Value("${spring.task.execution.pools.bigTask.keep-alive:300}")
    private int bigTaskKeepAliveSeconds;
    @Value("${spring.task.execution.pools.bigTask.allow-core-thread-timeout:true}")
    private boolean bigTaskAllowCoreThreadTimeOut;

    /**
     * 大任务线程池（如计划任务） - 池子小，大任务，耗时长，使用频率低，保活时间长，长时间不用可以回收核心线程
     *
     * @return 线程池
     */
    @Bean("bigTask")
    public Executor bigTask() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(bigTaskCorePoolSize);
        executor.setMaxPoolSize(bigTaskMaxPoolSize);
        executor.setQueueCapacity(bigTaskQueueCapacity);
        executor.setThreadNamePrefix(bigTaskThreadNamePrefix);
        executor.setKeepAliveSeconds(bigTaskKeepAliveSeconds);
        executor.setAllowCoreThreadTimeOut(bigTaskAllowCoreThreadTimeOut);
        executor.setRejectedExecutionHandler(new MonitoringCallerRunsPolicy("bigTask"));
        executor.initialize();
        log.info("ThreadPoolTaskExecutor 初始化完成: core={}, max={}, queue={}, prefix={}",
                bigTaskCorePoolSize, bigTaskMaxPoolSize, bigTaskQueueCapacity, bigTaskThreadNamePrefix);
        return executor;
    }

}
