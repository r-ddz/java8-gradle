package com.ddz.demo.okhttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class OkHttpUtil {


    private OkHttpClient okHttpClient;

    private final OkHttpConfig okHttpConfig;

    // JSON媒体类型常量
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    @Autowired
    public OkHttpUtil(OkHttpConfig okHttpConfig) {
        this.okHttpConfig = okHttpConfig;
    }

    /**
     * 初始化OkHttpClient
     * 使用@PostConstruct确保在Bean初始化后执行
     */
    @PostConstruct
    public void init() {
        log.info("正在初始化OkHttpClient，配置：{}", okHttpConfig);

        // 创建连接池
        ConnectionPool connectionPool = new ConnectionPool(
                okHttpConfig.getMaxIdleConnections(),
                okHttpConfig.getKeepAliveDuration(),
                okHttpConfig.getKeepAliveTimeUnit()
        );

        // 创建Dispatcher（调度器），控制并发请求
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(okHttpConfig.getMaxRequests());
        dispatcher.setMaxRequestsPerHost(okHttpConfig.getMaxRequestsPerHost());

        // 构建OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                // 连接超时
                .connectTimeout(okHttpConfig.getConnectTimeout(), okHttpConfig.getTimeoutUnit())
                // 读取超时
                .readTimeout(okHttpConfig.getReadTimeout(), okHttpConfig.getTimeoutUnit())
                // 写入超时
                .writeTimeout(okHttpConfig.getWriteTimeout(), okHttpConfig.getTimeoutUnit())
                // 连接池
                .connectionPool(connectionPool)
                // 调度器
                .dispatcher(dispatcher)
                // 连接失败重试
                .retryOnConnectionFailure(okHttpConfig.isRetryOnConnectionFailure());

        // 添加拦截器（可选）
        builder.addInterceptor(new LoggingInterceptor());

        this.okHttpClient = builder.build();

        log.info("OkHttpClient初始化完成，连接池配置：最大空闲连接={}，保活时间={}{}",
                okHttpConfig.getMaxIdleConnections(),
                okHttpConfig.getKeepAliveDuration(),
                okHttpConfig.getKeepAliveTimeUnit());
    }

    /**
     * Bean销毁时关闭连接池
     */
    @PreDestroy
    public void destroy() {
        if (okHttpClient != null) {
            // 关闭连接池，释放资源
            okHttpClient.dispatcher().executorService().shutdown();
            okHttpClient.connectionPool().evictAll();
            log.info("OkHttpClient资源已释放");
        }
    }

    /**
     * 同步GET请求
     * @param url 请求地址
     * @param headers 请求头
     * @return 响应字符串
     * @throws IOException 网络异常
     */
    public String doGet(String url, Map<String, String> headers) throws IOException {
        return doGet(url, headers, null);
    }

    /**
     * 同步GET请求（带查询参数）
     * @param url 请求地址
     * @param headers 请求头
     * @param queryParams 查询参数
     * @return 响应字符串
     * @throws IOException 网络异常
     */
    public String doGet(String url, Map<String, String> headers, Map<String, String> queryParams) throws IOException {
        Request.Builder builder = new Request.Builder();

        // 构建带查询参数的URL
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            throw new IllegalArgumentException("URL格式错误: " + url);
        }

        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        if (queryParams != null && !queryParams.isEmpty()) {
            queryParams.forEach(urlBuilder::addQueryParameter);
        }

        builder.url(urlBuilder.build());
        addHeaders(builder, headers);

        return executeRequest(builder.build());
    }

    /**
     * 同步POST请求（JSON格式）
     * @param url 请求地址
     * @param jsonBody JSON请求体
     * @param headers 请求头
     * @return 响应字符串
     * @throws IOException 网络异常
     */
    public String doPostJson(String url, String jsonBody, Map<String, String> headers) throws IOException {
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        addHeaders(builder, headers);

        return executeRequest(builder.build());
    }

    /**
     * 同步POST请求（表单格式）
     * @param url 请求地址
     * @param formParams 表单参数
     * @param headers 请求头
     * @return 响应字符串
     * @throws IOException 网络异常
     */
    public String doPostForm(String url, Map<String, String> formParams, Map<String, String> headers) throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (formParams != null && !formParams.isEmpty()) {
            formParams.forEach(formBuilder::add);
        }

        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(formBuilder.build());
        addHeaders(builder, headers);

        return executeRequest(builder.build());
    }

    /**
     * 同步PUT请求（JSON格式）
     * @param url 请求地址
     * @param jsonBody JSON请求体
     * @param headers 请求头
     * @return 响应字符串
     * @throws IOException 网络异常
     */
    public String doPutJson(String url, String jsonBody, Map<String, String> headers) throws IOException {
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .put(body);
        addHeaders(builder, headers);

        return executeRequest(builder.build());
    }

    /**
     * 同步DELETE请求
     * @param url 请求地址
     * @param headers 请求头
     * @return 响应字符串
     * @throws IOException 网络异常
     */
    public String doDelete(String url, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .delete();
        addHeaders(builder, headers);

        return executeRequest(builder.build());
    }

    /**
     * 异步GET请求
     * @param url 请求地址
     * @param headers 请求头
     * @param callback 回调函数
     */
    public void doGetAsync(String url, Map<String, String> headers, Callback callback) {
        Request.Builder builder = new Request.Builder().url(url);
        addHeaders(builder, headers);
        enqueueRequest(builder.build(), callback);
    }

    /**
     * 异步POST请求（JSON格式）
     * @param url 请求地址
     * @param jsonBody JSON请求体
     * @param headers 请求头
     * @param callback 回调函数
     */
    public void doPostJsonAsync(String url, String jsonBody, Map<String, String> headers, Callback callback) {
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        addHeaders(builder, headers);
        enqueueRequest(builder.build(), callback);
    }

    /**
     * 执行同步请求
     * @param request 请求对象
     * @return 响应字符串
     * @throws IOException 网络异常
     */
    private String executeRequest(Request request) throws IOException {
        log.debug("发送请求: {} {}", request.method(), request.url());

        long startTime = System.currentTimeMillis();
        try (Response response = okHttpClient.newCall(request).execute()) {
            long costTime = System.currentTimeMillis() - startTime;

            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "";
                log.error("请求失败: {} {}，状态码: {}，耗时: {}ms",
                        request.method(), request.url(), response.code(), costTime);
                throw new IOException("HTTP " + response.code() + ": " + errorBody);
            }

            String responseBody = response.body() != null ? response.body().string() : "";
            log.debug("请求成功: {} {}，状态码: {}，耗时: {}ms，响应长度: {}",
                    request.method(), request.url(), response.code(), costTime, responseBody.length());

            return responseBody;
        }
    }

    /**
     * 执行异步请求
     * @param request 请求对象
     * @param callback 回调函数
     */
    private void enqueueRequest(Request request, Callback callback) {
        log.debug("发送异步请求: {} {}", request.method(), request.url());
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 添加请求头
     * @param builder 请求构建器
     * @param headers 请求头Map
     */
    private void addHeaders(Request.Builder builder, Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(builder::addHeader);
        }
    }

    /**
     * 获取原始OkHttpClient实例（用于高级操作）
     * @return OkHttpClient实例
     */
    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * 获取连接池统计信息
     * @return 统计信息字符串
     */
    public String getConnectionPoolStats() {
        ConnectionPool pool = okHttpClient.connectionPool();
        return String.format("连接池状态: 连接总数=%d, 空闲连接数=%d",
                pool.connectionCount(),
                pool.idleConnectionCount());
    }

}
