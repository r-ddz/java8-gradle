package com.ddz.demo.thread.core.pooltask.config;

import com.ddz.demo.thread.common.decorator.LogTaskDecorator;
import com.ddz.demo.thread.common.factory.DefaultThreadFactory;
import com.ddz.demo.thread.common.policy.RejectPolicyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * 默认的异步任务配置
 *
 * Spring 在为 @Async 寻找默认执行器时遵循一个明确的查找链：
 *      第一优先级：AsyncConfigurer.getAsyncExecutor() 方法的返回值。这是 Spring 首先、也是最明确寻找的地方。
 *      第二优先级：查找容器中是否存在名为 taskExecutor 的 Executor 类型 Bean。
 *      第三优先级：查找容器中是否存在带有 @Primary 注解的 Executor 类型 Bean。
 *      第四优先级：查找任意一个 Executor 类型的 Bean（如果存在多个则会报错）。
 *      最终回退：如果以上都未找到，则创建一个简单的 SimpleAsyncTaskExecutor。
 *
 * @author zr
 */
@Slf4j
//@Configuration
public class DefaultAsyncConfig implements AsyncConfigurer {

    /**
     * 定义使用 @Async 时，Spring 最先寻找的 bean
     *      因为实现了 AsyncConfigurer 接口
     *      这里的 @Primary 注解和明文定义 bean name 都不是必须的
     *      之所以写上，是为了保障可读性，以及后续即便不再实现 AsyncConfigurer 也有兜底的配置
     *
     * @return 默认的线程池
     */
    @Bean(name = "taskExecutor")
    @Primary
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // ========== 基本配置（对应YAML中的配置）==========
        // 1. 核心线程数 - 线程池维护的最小线程数
        //    对应YAML: spring.task.execution.pool.core-size
        executor.setCorePoolSize(8);

        // 2. 最大线程数 - 当队列满时，最多可以创建的线程数
        //    对应YAML: spring.task.execution.pool.max-size
        executor.setMaxPoolSize(16);

        // 3. 队列容量 - 任务队列的大小
        //    对应YAML: spring.task.execution.pool.queue-capacity
        executor.setQueueCapacity(50);

        // 4. 线程名前缀 - 便于日志追踪和监控
        //    对应YAML: spring.task.execution.thread-name-prefix
        // 不设置工厂才使用这里的线程前缀名，否则以工厂中定义的线程名前缀为准
        executor.setThreadNamePrefix("task-01-");

        // 5. 线程空闲时间 - 非核心线程空闲多久后被回收
        //    对应YAML: spring.task.execution.pool.keep-alive
        executor.setKeepAliveSeconds(60);

        // 6. 是否允许核心线程超时 - 核心线程空闲时是否可以被回收
        //    对应YAML: spring.task.execution.pool.allow-core-thread-timeout
        executor.setAllowCoreThreadTimeOut(false);


        // ========== 高级配置（YAML无法配置的部分）==========

        // 7. 拒绝策略 - YAML无法配置，必须通过代码设置
        //    ThreadPoolExecutor提供了4种内置策略：
        //    - AbortPolicy（默认）：抛出RejectedExecutionException
        //    - CallerRunsPolicy：由调用线程执行任务
        //    - DiscardPolicy：直接丢弃任务
        //    - DiscardOldestPolicy：丢弃队列中最老的任务
        //    - 也可以自定义拒绝策略

        executor.setRejectedExecutionHandler(RejectPolicyFactory.callerRunsPolicy());
        // 生产环境建议使用CallerRunsPolicy，避免任务丢失

        // 8. 自定义线程工厂 - 可以控制线程的创建方式
        //    可以设置线程优先级、守护线程、异常处理器等
        executor.setThreadFactory(new DefaultThreadFactory("thread-01-"));

        // 9. 任务装饰器 - 可以在任务执行前后添加额外逻辑
        //    常用于传递上下文（如MDC日志追踪）、监控、性能统计等
        executor.setTaskDecorator(new LogTaskDecorator());

        // 10. 等待任务完成关闭 - 优雅关闭
        //     应用关闭时是否等待任务完成
        executor.setWaitForTasksToCompleteOnShutdown(true);

        // 11. 等待关闭的最大时间
        executor.setAwaitTerminationSeconds(30);

        // 12. 初始化线程池（必须调用！）
        executor.initialize();

        log.info("ThreadPoolTaskExecutor 初始化完成: core={}, max={}, queue={}, prefix={}",
                8, 16, 50, "thread-01-");
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                log.error("异步方法执行失败 - 方法: {}, 参数: {}", method.getName(), params, ex);

                // 可以根据异常类型做不同的处理
                if (ex instanceof RuntimeException) {
                    // 业务异常，记录但不告警
                    log.warn("业务异常: {}", ex.getMessage());
                } else {
                    // 系统异常，发送告警
                    log.warn("系统异常: {}", ex.getMessage());
                }
                // 记录监控指标
            }
        };
    }

}
