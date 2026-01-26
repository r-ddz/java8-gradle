package com.ddz.demo.thread.core.pooltask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

// 配置类2：IO密集型任务专用池
//@Configuration
public class IoThreadPoolTaskConfig {

    @Bean("ioExecutor") // 明确命名
    public Executor ioExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);
        executor.setMaxPoolSize(200);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("async-io-");
        executor.initialize();
        return executor;
    }
}
