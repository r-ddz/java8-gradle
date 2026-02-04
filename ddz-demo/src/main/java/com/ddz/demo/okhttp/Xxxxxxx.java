//package com.ddz.demo.okhttp;
//
//import okhttp3.*;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//public class Xxxxxxx {
//
//    private static OkHttpClient OK_HTTP_CLIENT;
//
//    // JSON媒体类型常量
//    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//    private static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
//
//
//    static {
//        // 初始化OkHttpClient
//        init();
//        // 添加JVM关闭钩子，优雅释放资源（重要！）
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            System.out.println("JVM关闭，正在释放OkHttpClient资源...");
//            // 关闭连接池，取消所有请求
//            destroy();
//        }));
//    }
//
//    /**
//     * 初始化OkHttpClient
//     * 使用@PostConstruct确保在Bean初始化后执行
//     */
//    public static void init() {
//        // 创建连接池，最大空闲连接数5，保活5分钟
//        ConnectionPool connectionPool = new ConnectionPool(5, 5, TimeUnit.MINUTES);
//
//        // 创建Dispatcher（调度器），控制并发请求【相当于一个简易版的熔断限流组件】，多的并发请求在队列排队，无法限制队列大小和拒绝策略，如果系统并发量远超极限，这个队列可能会过大，导致内存溢出
//        Dispatcher dispatcher = new Dispatcher();
//        // 整个客户端同时执行的最大请求数，相当于限制了最大连接数
//        dispatcher.setMaxRequests(64);
//        // 每主机(host)最大请求数
//        dispatcher.setMaxRequestsPerHost(5);
//
//        // 构建OkHttpClient
//        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                // 连接超时 10 秒
//                .connectTimeout(10, TimeUnit.SECONDS)
//                // 读取超时 30 秒
//                .readTimeout(30, TimeUnit.SECONDS)
//                // 写入超时 10 秒
//                .writeTimeout(10, TimeUnit.SECONDS)
//                // 连接池
//                .connectionPool(connectionPool)
//                // 调度器
//                .dispatcher(dispatcher)
//                // 连接失败重试，对于可重试的失败请求自动重试【如果有自定义重试的需求，可以利用自定义拦截器实现】
//                .retryOnConnectionFailure(true);
//
//        // 添加拦截器（可选）
//        builder.addInterceptor(new LoggingInterceptor());
//        OK_HTTP_CLIENT = builder.build();
//    }
//
//    /**
//     * Bean销毁时关闭连接池
//     */
//    public static void destroy() {
//        if (OK_HTTP_CLIENT != null) {
//            // 关闭连接池，释放资源
//            OK_HTTP_CLIENT.dispatcher().cancelAll();
//            OK_HTTP_CLIENT.connectionPool().evictAll();
//            OK_HTTP_CLIENT.dispatcher().executorService().shutdown();
//        }
//    }
//    // ... 已有的静态变量和初始化代码 ...
//
//    // 添加常量
//    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");
//    private static final MediaType TEXT = MediaType.parse("text/plain; charset=utf-8");
//    private static final MediaType OCTET_STREAM = MediaType.parse("application/octet-stream");
//
//    /**
//     * 同步PUT请求（JSON格式）
//     */
//    public static String doPutJson(String url, String json, Map<String, String> headers) throws IOException {
//        RequestBody body = RequestBody.create(json, JSON);
//        return executeRequestWithBody(url, "PUT", body, headers);
//    }
//
//    /**
//     * 同步DELETE请求
//     */
//    public static String doDelete(String url, Map<String, String> headers) throws IOException {
//        return doDelete(url, null, headers);
//    }
//
//    /**
//     * 同步DELETE请求（带请求体）
//     */
//    public static String doDelete(String url, String jsonBody, Map<String, String> headers) throws IOException {
//        Request request;
//        Request.Builder builder = new Request.Builder().url(url);
//
//        if (jsonBody != null && !jsonBody.isEmpty()) {
//            RequestBody body = RequestBody.create(jsonBody, JSON);
//            builder.delete(body);
//        } else {
//            builder.delete();
//        }
//
//        addHeaders(builder, headers);
//        request = builder.build();
//        return executeRequest(request);
//    }
//
//    /**
//     * 同步POST请求（XML格式）
//     */
//    public static String doPostXml(String url, String xml, Map<String, String> headers) throws IOException {
//        RequestBody body = RequestBody.create(xml, XML);
//        return executeRequestWithBody(url, "POST", body, headers);
//    }
//
//    /**
//     * 同步POST请求（文本格式）
//     */
//    public static String doPostText(String url, String text, Map<String, String> headers) throws IOException {
//        RequestBody body = RequestBody.create(text, TEXT);
//        return executeRequestWithBody(url, "POST", body, headers);
//    }
//
//    /**
//     * 同步POST请求（表单格式）
//     */
//    public static String doPostForm(String url, Map<String, String> formData, Map<String, String> headers) throws IOException {
//        FormBody.Builder formBuilder = new FormBody.Builder();
//        if (formData != null && !formData.isEmpty()) {
//            formData.forEach(formBuilder::add);
//        }
//        RequestBody body = formBuilder.build();
//        return executeRequestWithBody(url, "POST", body, headers);
//    }
//
//    /**
//     * 文件上传（单文件）
//     */
//    public static String uploadFile(String url, File file, String fileParamName,
//                                    Map<String, String> formData, Map<String, String> headers) throws IOException {
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM);
//
//        // 添加文件
//        RequestBody fileBody = RequestBody.create(file, MediaType.parse(getMimeType(file)));
//        builder.addFormDataPart(fileParamName, file.getName(), fileBody);
//
//        // 添加其他表单字段
//        if (formData != null && !formData.isEmpty()) {
//            formData.forEach(builder::addFormDataPart);
//        }
//
//        RequestBody body = builder.build();
//        return executeRequestWithBody(url, "POST", body, headers);
//    }
//
//    /**
//     * 多文件上传
//     */
//    public static String uploadMultipleFiles(String url, Map<String, File> files,
//                                             Map<String, String> formData, Map<String, String> headers) throws IOException {
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM);
//
//        // 添加多个文件
//        if (files != null && !files.isEmpty()) {
//            files.forEach((paramName, file) -> {
//                RequestBody fileBody = RequestBody.create(file, MediaType.parse(getMimeType(file)));
//                builder.addFormDataPart(paramName, file.getName(), fileBody);
//            });
//        }
//
//        // 添加其他表单字段
//        if (formData != null && !formData.isEmpty()) {
//            formData.forEach(builder::addFormDataPart);
//        }
//
//        RequestBody body = builder.build();
//        return executeRequestWithBody(url, "POST", body, headers);
//    }
//
//    /**
//     * 文件下载（保存到指定路径）
//     */
//    public static void downloadFile(String url, String savePath, Map<String, String> headers) throws IOException {
//        downloadFile(url, savePath, null, headers);
//    }
//
//    /**
//     * 文件下载（保存到指定路径，可指定文件名）
//     */
//    public static void downloadFile(String url, String savePath, String fileName, Map<String, String> headers) throws IOException {
//        Request.Builder builder = new Request.Builder().url(url);
//        addHeaders(builder, headers);
//        Request request = builder.build();
//
//        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new IOException("下载失败，状态码: " + response.code());
//            }
//
//            ResponseBody body = response.body();
//            if (body == null) {
//                throw new IOException("响应体为空");
//            }
//
//            // 如果未指定文件名，尝试从响应头获取
//            if (fileName == null) {
//                fileName = getFileNameFromHeaders(response);
//                if (fileName == null) {
//                    fileName = "downloaded_file_" + System.currentTimeMillis();
//                }
//            }
//
//            File outputFile = new File(savePath, fileName);
//            try (InputStream inputStream = body.byteStream();
//                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {
//
//                byte[] buffer = new byte[4096];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//            }
//        }
//    }
//
//    /**
//     * 文件下载（返回字节数组）
//     */
//    public static byte[] downloadFileAsBytes(String url, Map<String, String> headers) throws IOException {
//        Request.Builder builder = new Request.Builder().url(url);
//        addHeaders(builder, headers);
//        Request request = builder.build();
//
//        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new IOException("下载失败，状态码: " + response.code());
//            }
//
//            ResponseBody body = response.body();
//            if (body == null) {
//                throw new IOException("响应体为空");
//            }
//
//            return body.bytes();
//        }
//    }
//
//    /**
//     * 异步GET请求
//     */
//    public static void doGetAsync(String url, Map<String, String> headers, Callback callback) {
//        Request.Builder builder = new Request.Builder().url(url);
//        addHeaders(builder, headers);
//        Request request = builder.build();
//        OK_HTTP_CLIENT.newCall(request).enqueue(callback);
//    }
//
//    /**
//     * 异步POST请求（JSON）
//     */
//    public static void doPostJsonAsync(String url, String json, Map<String, String> headers, Callback callback) {
//        RequestBody body = RequestBody.create(json, JSON);
//        Request.Builder builder = new Request.Builder()
//                .url(url)
//                .post(body);
//        addHeaders(builder, headers);
//        Request request = builder.build();
//        OK_HTTP_CLIENT.newCall(request).enqueue(callback);
//    }
//
//    /**
//     * PATCH请求（JSON）
//     */
//    public static String doPatchJson(String url, String json, Map<String, String> headers) throws IOException {
//        RequestBody body = RequestBody.create(json, JSON);
//        return executeRequestWithBody(url, "PATCH", body, headers);
//    }
//
//    /**
//     * HEAD请求
//     */
//    public static Response doHead(String url, Map<String, String> headers) throws IOException {
//        Request.Builder builder = new Request.Builder()
//                .url(url)
//                .head();
//        addHeaders(builder, headers);
//        Request request = builder.build();
//        return OK_HTTP_CLIENT.newCall(request).execute();
//    }
//
//    /**
//     * OPTIONS请求
//     */
//    public static String doOptions(String url, Map<String, String> headers) throws IOException {
//        Request.Builder builder = new Request.Builder()
//                .url(url)
//                .method("OPTIONS", null);
//        addHeaders(builder, headers);
//        Request request = builder.build();
//        return executeRequest(request);
//    }
//
//    // ============== 私有辅助方法 ==============
//
//    private static String executeRequestWithBody(String url, String method, RequestBody body,
//                                                 Map<String, String> headers) throws IOException {
//        Request.Builder builder = new Request.Builder()
//                .url(url)
//                .method(method, body);
//        addHeaders(builder, headers);
//        Request request = builder.build();
//        return executeRequest(request);
//    }
//
//    private static String executeRequest(Request request) throws IOException {
//        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                String errorBody = response.body() != null ? response.body().string() : "";
//                throw new IOException("HTTP " + response.code() + ": " + errorBody);
//            }
//            return response.body() != null ? response.body().string() : "";
//        }
//    }
//
//    private static void addHeaders(Request.Builder builder, Map<String, String> headers) {
//        if (headers != null && !headers.isEmpty()) {
//            headers.forEach(builder::addHeader);
//        }
//    }
//
//    private static String getMimeType(File file) {
//        try {
//            String mimeType = Files.probeContentType(file.toPath());
//            return mimeType != null ? mimeType : "application/octet-stream";
//        } catch (IOException e) {
//            return "application/octet-stream";
//        }
//    }
//
//    private static String getFileNameFromHeaders(Response response) {
//        String contentDisposition = response.header("Content-Disposition");
//        if (contentDisposition != null && contentDisposition.contains("filename=")) {
//            return contentDisposition.split("filename=")[1].replace("\"", "");
//        }
//        return null;
//    }
//
//
//
//}
