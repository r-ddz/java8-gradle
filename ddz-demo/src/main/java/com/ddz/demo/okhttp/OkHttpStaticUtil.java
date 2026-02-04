package com.ddz.demo.okhttp;

import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpStaticUtil {

    private static OkHttpClient OK_HTTP_CLIENT;

    // JSON媒体类型常量
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");
    private static final MediaType TEXT = MediaType.parse("text/plain; charset=utf-8");


    static {
        // 初始化OkHttpClient
        init();
        // 添加JVM关闭钩子，释放资源（重要！）
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("JVM关闭，正在释放OkHttpClient资源...");
            // 关闭连接池，取消所有请求
            destroy();
        }));
    }

    /**
     * 初始化OkHttpClient
     * 使用@PostConstruct确保在Bean初始化后执行
     */
    public static void init() {
        // 创建连接池，最大空闲连接数5，保活5分钟
        ConnectionPool connectionPool = new ConnectionPool(5, 5, TimeUnit.MINUTES);

        // 创建Dispatcher（调度器），控制并发请求【相当于一个简易版的熔断限流组件】，多的并发请求在队列排队，无法限制队列大小和拒绝策略，如果系统并发量远超极限，这个队列可能会过大，导致内存溢出
        Dispatcher dispatcher = new Dispatcher();
        // 整个客户端同时执行的最大请求数，相当于限制了最大连接数
        dispatcher.setMaxRequests(64);
        // 每主机(host)最大请求数
        dispatcher.setMaxRequestsPerHost(5);

        // 构建OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                // 连接超时 10 秒 TODO 调试过程中暂时加大
                .connectTimeout(10*1000, TimeUnit.SECONDS)
                // 读取超时 30 秒
                .readTimeout(30*1000, TimeUnit.SECONDS)
                // 写入超时 10 秒
                .writeTimeout(10*1000, TimeUnit.SECONDS)
                // 连接池
                .connectionPool(connectionPool)
                // 调度器
                .dispatcher(dispatcher)
                // 连接失败重试，对于可重试的失败请求自动重试【如果有自定义重试的需求，可以利用自定义拦截器实现】
                .retryOnConnectionFailure(true);

        // 添加拦截器（可选）
//        builder.addInterceptor(new LoggingInterceptor());
        OK_HTTP_CLIENT = builder.build();
    }

    /**
     * Bean销毁时关闭连接池
     */
    public static void destroy() {
        if (OK_HTTP_CLIENT != null) {
            // 关闭连接池，释放资源
            OK_HTTP_CLIENT.dispatcher().cancelAll();
            OK_HTTP_CLIENT.connectionPool().evictAll();
            OK_HTTP_CLIENT.dispatcher().executorService().shutdown();
        }
    }


    /**
     * 同步GET请求（带查询参数）
     * @param url 请求地址
     * @param headers 请求头
     * @param params 请求参数
     * @return 响应字符串
     * @throws IOException 网络异常
     */
    public static String get(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        // 构建带查询参数的URL
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            throw new IllegalArgumentException("URL格式错误: " + url);
        }
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        if (params != null && !params.isEmpty()) {
            params.forEach(urlBuilder::addQueryParameter);
        }

        Request.Builder builder = new Request.Builder()
                .url(urlBuilder.build())
                .tag(String.class, "")
                .tag(Boolean.class, false)
                .get(); // new Request.Builder() 默认就是 get 这里也可以不调用 .get()
        addHeaders(builder, headers);
        return executeRequest(builder.build());
    }

    public static String postJson(String url, String jsonBody, Map<String, String> headers) throws IOException {
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        addHeaders(builder, headers);
        return executeRequest(builder.build());
    }

    public static void postJsonAsync(String url, String jsonBody, Map<String, String> headers, Callback callback) throws IOException {
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        addHeaders(builder, headers);
        enqueueRequest(builder.build(), callback);
    }

    public static String postXml(String url, String xmlBody, Map<String, String> headers) throws IOException {
        RequestBody body = RequestBody.create(xmlBody, XML);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        addHeaders(builder, headers);
        return executeRequest(builder.build());
    }

    /**
     * 同步POST请求（文本格式）
     */
    public static String postText(String url, String text, Map<String, String> headers) throws IOException {
        RequestBody body = RequestBody.create(text, TEXT);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        addHeaders(builder, headers);
        return executeRequest(builder.build());
    }

    /**
     * 同步POST请求（表单格式）
     */
    public static String postForm(String url, Map<String, String> formData, Map<String, String> headers) throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (formData != null && !formData.isEmpty()) {
            formData.forEach(formBuilder::add);
        }
        RequestBody body = formBuilder.build();
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
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
    public static String putJson(String url, String jsonBody, Map<String, String> headers) throws IOException {
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
    public static String deleteJson(String url, String jsonBody, Map<String, String> headers) throws IOException {
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .delete(body);
        addHeaders(builder, headers);
        return executeRequest(builder.build());
    }

    /**
     * 文件上传（单文件）
     * fileParamName = "file" 要与这里对应 @RequestParam("file") MultipartFile file
     */
    public static String uploadFile(String url, File file, String fileParamName,
                                    Map<String, String> formData, Map<String, String> headers) throws IOException {
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody fileBody = RequestBody.create(file, MediaType.parse(getMimeType(file)));
        bodyBuilder.addFormDataPart(fileParamName, file.getName(), fileBody);
        if (formData != null && !formData.isEmpty()) {
            formData.forEach(bodyBuilder::addFormDataPart);
        }
        RequestBody body = bodyBuilder.build();
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        addHeaders(builder, headers);
        Request request = builder.build();
        return executeRequest(request);
    }

    private static String getMimeType(File file) {
        try {
            String mimeType = Files.probeContentType(file.toPath());
            return mimeType != null ? mimeType : "application/octet-stream";
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    private static String executeRequestWithBody(String url, String method, RequestBody body,
                                                 Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .method(method, body);
        addHeaders(builder, headers);
        Request request = builder.build();
        return executeRequest(request);
    }

    /**
     * 执行同步请求
     * @param request 请求对象
     * @return 响应字符串
     * @throws IOException 网络异常
     */
    private static String executeRequest(Request request) throws IOException {
        Response response = null;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            String responseBody = response.body() != null ? response.body().string() : "";
            if (!response.isSuccessful()) {
                throw new IOException("HTTP " + response.code() + ": " + responseBody);
            }
            return responseBody;
        } finally {
            // 释放 response 资源
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * 执行异步请求
     * @param request 请求对象
     * @param callback 回调函数
     */
    private static void enqueueRequest(Request request, Callback callback) {
        OK_HTTP_CLIENT.newCall(request).enqueue(callback);
    }

    /**
     * 添加请求头
     * @param builder 请求构建器
     * @param headers 请求头Map
     */
    private static void addHeaders(Request.Builder builder, Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(builder::addHeader);
        }
    }

    /**
     * 获取原始OkHttpClient实例（用于高级操作）
     * @return OkHttpClient实例
     */
    public static OkHttpClient getOkHttpClient() {
        return OK_HTTP_CLIENT;
    }

    /**
     * 监控小工具：获取连接池统计信息
     * @return 统计信息字符串
     */
    public String getPoolStats() {
        ConnectionPool pool = OK_HTTP_CLIENT.connectionPool();
        Dispatcher dispatcher = OK_HTTP_CLIENT.dispatcher();
        return String.format("连接池状态: 连接总数=%d, 空闲连接数=%d, 正在执行的异步请求数=%d, 等待执行的异步请求数=%d",
                pool.connectionCount(),
                pool.idleConnectionCount(),
                dispatcher.runningCallsCount(),
                dispatcher.queuedCallsCount());
    }

}
