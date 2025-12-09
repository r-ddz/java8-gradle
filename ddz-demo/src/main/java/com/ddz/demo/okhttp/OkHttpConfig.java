package com.ddz.demo.okhttp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
//@ConfigurationProperties(prefix = "okhttp") // 可以读取配置文件
@Data
public class OkHttpConfig {

    /**
     * 连接超时时间 - 建立TCP连接的最大等待时间
     * 默认：10秒，建议根据网络状况调整
     */
    private long connectTimeout = 10;

    /**
     * 读取超时时间 - 从服务器读取数据的最大等待时间
     * 默认：30秒，对于大数据量接口可适当增加
     */
    private long readTimeout = 30;

    /**
     * 写入超时时间 - 向服务器发送数据的最大等待时间
     * 默认：10秒，对于大文件上传可适当增加
     */
    private long writeTimeout = 10;

    /**
     * 连接池最大空闲连接数 - 同时保持的最大空闲连接数
     * 默认：5，过大会浪费资源，过小无法有效复用连接
     */
    private int maxIdleConnections = 5;

    /**
     * 连接保活时间 - 空闲连接在连接池中保留的时间
     * 默认：5分钟，超过此时间未使用会被释放
     */
    private long keepAliveDuration = 5;

    /**
     * 最大请求数 - 整个客户端同时执行的最大请求数
     * 默认：64，限制全局并发，防止资源耗尽
     */
    private int maxRequests = 64;

    /**
     * 每主机最大请求数 - 对同一域名同时执行的最大请求数
     * 默认：5，避免对单一服务器造成过大压力
     */
    private int maxRequestsPerHost = 5;

    /**
     * 超时时间单位，默认：秒
     */
    private TimeUnit timeoutUnit = TimeUnit.SECONDS;

    /**
     * 连接保活时间单位，默认：分钟
     */
    private TimeUnit keepAliveTimeUnit = TimeUnit.MINUTES;

    /**
     * 是否开启重试 - 对于可重试的失败请求自动重试
     * 默认：开启，对幂等操作友好
     */
    private boolean retryOnConnectionFailure = true;

    /**
     * 是否开启Gzip压缩 - 自动处理响应压缩
     * 默认：开启，减少网络传输量
     */
    private boolean gzipEnabled = true;

}
