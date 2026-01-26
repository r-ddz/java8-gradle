package com.ddz.demo.thread.common.decorator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskDecorator;

/**
 * 日志任务装饰器
 *
 * @author zr
 */
@Slf4j
public class LogTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        return () -> {
            try {
                log.error("装饰器执行原始任务 之前 日志");
                // 执行原始任务
                runnable.run();
                log.error("装饰器执行原始任务 之后 日志");
            } catch (Throwable e) {
                log.error("装饰器 LogTaskDecorator 捕获异常: {}", e.getMessage(), e);
                throw e;
            }
        };
    }

}
