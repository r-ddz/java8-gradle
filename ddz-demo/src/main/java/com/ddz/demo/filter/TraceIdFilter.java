package com.ddz.demo.filter;

import cn.hutool.core.util.StrUtil;
import com.ddz.core.utils.TraceIdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Trace ID 过滤器
 * 为每个 HTTP 请求生成/传递 Trace ID
 */
@Component
@Order(Integer.MIN_VALUE)  // 确保最先执行
public class TraceIdFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(TraceIdFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 防止脏数据，这里先清一次
        TraceIdUtil.clear();

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 先尝试从请求头获取 Trace ID
        String traceId = httpRequest.getHeader(TraceIdUtil.TRACE_ID_HEADER);

        // 初始化 Trace ID, 如果为空则生成一个
        traceId = TraceIdUtil.initTraceId(traceId);

        // 将 Trace ID 添加到响应头
        httpResponse.addHeader(TraceIdUtil.TRACE_ID_HEADER, traceId);

        try {
            chain.doFilter(request, response);
        } finally {
            // 清除Trace ID
            TraceIdUtil.clear();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        logger.info("TraceIdFilter 初始化完成");
    }

    @Override
    public void destroy() {
        logger.info("TraceIdFilter 销毁");
    }
}