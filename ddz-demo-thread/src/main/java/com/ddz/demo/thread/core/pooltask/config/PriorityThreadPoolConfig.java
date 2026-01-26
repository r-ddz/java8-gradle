package com.ddz.demo.thread.core.pooltask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

// 配置类4：高优先级任务专用池（例如支付回调）
//@Configuration
public class PriorityThreadPoolConfig {

    @Bean("priorityExecutor")
    public Executor priorityExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-priority-");
        executor.setThreadPriority(Thread.MAX_PRIORITY); // 设置高优先级，注意如果自定义工厂，那么工厂赋值的优先级会覆盖此设置
        executor.initialize();
        return executor;
    }
}
