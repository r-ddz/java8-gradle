package com.ddz.demo.okhttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * 这是一个自定义重试的拦截器模板，有需要可以自定义
 */
@Slf4j
@Component
public class RetryInterceptor implements Interceptor {

    /**
     * 最大重试次数 - 从配置文件读取，例如：okhttp.retry.max-attempts=3
     */
    @Value("${okhttp.retry.max-attempts:3}")
    private int maxRetryAttempts;

    /**
     * 重试的基础间隔时间（毫秒）- 用于指数退避计算
     */
    @Value("${okhttp.retry.base-delay-ms:1000}")
    private long baseDelayMs;

    /**
     * 自定义：哪些HTTP状态码需要重试？
     * 通常对于5xx服务器错误和某些特定的4xx错误（如429 Too Many Requests）进行重试。
     */
    private final Predicate<Integer> retryableStatusCodes = code -> code >= 500 || code == 429;

    /**
     * 自定义：哪些IO异常需要重试？
     * 这里可以定义你认为需要重试的特定网络异常。
     */
    private final Predicate<IOException> retryableExceptions = e -> {
        String message = e.getMessage();
        // 重试连接超时、读超时、连接重置等可恢复异常
        return message != null && (
                message.contains("timeout") ||
                        message.contains("reset") ||
                        message.contains("unreachable")
        );
    };

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        IOException exception = null;

        // 开始重试循环
        for (int attempt = 1; attempt <= maxRetryAttempts; attempt++) {
            try {
                // 如果不是第一次尝试，则等待一段时间（指数退避）
                if (attempt > 1) {
                    long waitTime = calculateBackoffDelay(attempt);
                    log.warn("请求重试中，第{}次尝试，等待{}ms后执行。URL: {}",
                            attempt, waitTime, request.url());
                    Thread.sleep(waitTime);
                }

                // 执行请求
                response = chain.proceed(request);
                exception = null; // 清空之前的异常记录

                // 检查响应码，决定是否需要重试
                if (shouldRetryBasedOnResponse(response, attempt)) {
                    // 需要重试，则必须关闭当前响应体，否则会导致资源泄漏
                    closeResponseBody(response);
                    continue; // 继续下一次循环尝试
                }

                // 如果不需要重试，直接返回成功的响应
                return response;

            } catch (IOException e) {
                exception = e;
                // 检查异常，决定是否需要重试
                if (shouldRetryBasedOnException(e, attempt)) {
                    log.warn("请求遇到可重试异常，第{}次尝试。URL: {}, 异常: {}",
                            attempt, request.url(), e.getMessage());
                    continue; // 继续下一次循环尝试
                }
                // 如果是不可重试的异常，直接抛出
                throw e;
            } catch (InterruptedException e) {
                // 睡眠被中断，停止重试
                Thread.currentThread().interrupt();
                throw new IOException("重试过程被中断", e);
            }
        }

        // 如果重试次数用尽后仍然失败，抛出最后一次的异常
        if (exception != null) {
            log.error("请求重试{}次后最终失败。URL: {}", maxRetryAttempts, request.url(), exception);
            throw exception;
        }

        // 理论上不会走到这里，但为了编译器通过
        assert response != null;
        return response;
    }

    /**
     * 根据HTTP响应判断是否需要重试
     */
    private boolean shouldRetryBasedOnResponse(Response response, int attempt) {
        if (attempt >= maxRetryAttempts) {
            return false; // 已达最大重试次数
        }

        int code = response.code();
        String method = response.request().method();

        // 规则1：只对幂等方法重试（安全起见，特别是POST请求默认不重试）
        if (!isIdempotent(method)) {
            log.debug("请求方法 {} 非幂等，不进行重试。", method);
            return false;
        }

        // 规则2：根据状态码判断
        boolean shouldRetry = retryableStatusCodes.test(code);
        if (shouldRetry) {
            log.info("HTTP状态码 {} 触发重试策略，当前第{}次尝试。", code, attempt);
        }
        return shouldRetry;
    }

    /**
     * 根据异常判断是否需要重试
     */
    private boolean shouldRetryBasedOnException(IOException e, int attempt) {
        if (attempt >= maxRetryAttempts) {
            return false; // 已达最大重试次数
        }
        return retryableExceptions.test(e);
    }

    /**
     * 计算指数退避延迟时间（带随机抖动，防止惊群效应）
     */
    private long calculateBackoffDelay(int attempt) {
        // 指数退避公式：delay = base * 2^(attempt-1)
        long delay = (long) (baseDelayMs * Math.pow(2, attempt - 1));
        // 添加最多±20%的随机抖动
        double jitter = 0.8 + (Math.random() * 0.4); // 0.8 ~ 1.2
        return (long) (delay * jitter);
    }

    /**
     * 判断HTTP方法是否幂等
     */
    private boolean isIdempotent(String method) {
        // 根据HTTP规范，GET、HEAD、OPTIONS、TRACE、PUT、DELETE是幂等的
        switch (method.toUpperCase()) {
            case "GET":
            case "HEAD":
            case "OPTIONS":
            case "TRACE":
            case "PUT":
            case "DELETE":
                return true;
            case "POST":
                // POST默认不幂等，但你的业务如果确保幂等（如某类特定接口），可以在这里特殊处理
                return false; // 保守起见，设为false
            default:
                return false;
        }
    }

    /**
     * 安全关闭响应体，避免资源泄漏
     */
    private void closeResponseBody(Response response) {
        if (response != null && response.body() != null) {
            try {
                response.body().close();
            } catch (Exception ignored) {
                // 忽略关闭时的异常
            }
        }
    }
}
