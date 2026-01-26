package com.ddz.demo.thread.common.decorator;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * MDC任务装饰器 - 用于日志追踪
 * 在多线程环境中传递MDC上下文，确保日志能够追踪请求链路
 * YAML无法配置此功能
 *
 * @author zr
 */
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 获取当前线程的MDC上下文（日志追踪信息）
        Map<String, String> contextMap = MDC.getCopyOfContextMap();

        return () -> {
            try {
                // 在新线程中恢复MDC上下文
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }

                // 执行原始任务
                runnable.run();
            } finally {
                // 清理MDC上下文
                MDC.clear();
            }
        };
    }

}
