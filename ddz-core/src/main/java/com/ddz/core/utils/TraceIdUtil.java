package com.ddz.core.utils;

import cn.hutool.core.util.StrUtil;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Trace ID 工具类
 */
public class TraceIdUtil {

    // MDC 中的键名
    public static final String TRACE_ID_KEY = "traceId";

    // HTTP 请求头中的键名
    public static final String TRACE_ID_HEADER = "X-Trace-Id";

    /**
     * 获取当前 Trace ID
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    /**
     * 设置 Trace ID
     */
    public static void setTraceId(String traceId) {
        if (StrUtil.isNotBlank(traceId)) {
            MDC.put(TRACE_ID_KEY, traceId);
        }
    }

    /**
     * 生成 Trace ID
     */
    public static String generateTraceId() {
        // 生成16位的Trace ID（前8位时间戳 + 后8位随机数）
        long timestamp = System.currentTimeMillis() % 100000000;
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String traceId = String.format("%08x%08x", timestamp, random.hashCode() & 0xffffffffL);
        setTraceId(traceId);
        return traceId;
    }

    /**
     * 初始化 Trace ID
     * 如果已有则不重新生成
     */
    public static String initTraceId(String traceId) {
        if (StrUtil.isBlank(traceId)) {
            traceId = generateTraceId();
        }
        setTraceId(traceId);
        return traceId;
    }

    /**
     * 清除 Trace ID
     */
    public static void clear() {
        MDC.remove(TRACE_ID_KEY);
    }

}