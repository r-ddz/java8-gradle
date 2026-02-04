//package com.ddz.demo.http.controller;
//
//import com.ddz.demo.okhttp.OkHttpStaticUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletResponse;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Slf4j
//@Controller
//@RequestMapping("/demo") // 控制器级别的路径映射
//public class DemoController {
//
//
//    // 用于存储异步请求结果的临时缓存
//    private final Map<String, String> asyncResults = new ConcurrentHashMap<>();
//

//
//    // ==================== 文件上传测试 ====================
//
//    /**
//     * 单文件上传测试
//     */
//    @PostMapping("/test-upload-single")
//    public String testUploadSingle(@RequestParam String url,
//                                   @RequestParam("file") MultipartFile file,
//                                   @RequestParam(required = false) Map<String, String> formData,
//                                   @RequestHeader(required = false) Map<String, String> headers) {
//        try {
//            log.info("测试单文件上传: {}, 文件名: {}, 大小: {}字节",
//                    url, file.getOriginalFilename(), file.getSize());
//
//            // 将MultipartFile转换为临时文件
//            Path tempFile = Files.createTempFile("upload_", "_" + file.getOriginalFilename());
//            file.transferTo(tempFile.toFile());
//
//            // 执行上传
//            String result = OkHttpStaticUtil.uploadFile(url, tempFile.toFile(),
//                    "file", formData, headers);
//
//            // 清理临时文件
//            Files.deleteIfExists(tempFile);
//
//            return buildSuccessResponse("单文件上传成功", result);
//        } catch (Exception e) {
//            log.error("单文件上传失败", e);
//            return buildErrorResponse("单文件上传失败", e.getMessage());
//        }
//    }
//
//    /**
//     * 多文件上传测试
//     */
//    @PostMapping("/test-upload-multiple")
//    public String testUploadMultiple(@RequestParam String url,
//                                     @RequestParam("files") MultipartFile[] files,
//                                     @RequestParam(required = false) Map<String, String> formData,
//                                     @RequestHeader(required = false) Map<String, String> headers) {
//        try {
//            log.info("测试多文件上传: {}, 文件数量: {}", url, files.length);
//
//            Map<String, File> fileMap = new HashMap<>();
//            List<Path> tempFiles = new ArrayList<>();
//
//            // 将MultipartFile数组转换为File Map
//            for (int i = 0; i < files.length; i++) {
//                MultipartFile multipartFile = files[i];
//                Path tempFile = Files.createTempFile("upload_" + i + "_",
//                        "_" + multipartFile.getOriginalFilename());
//                multipartFile.transferTo(tempFile.toFile());
//                fileMap.put("file" + i, tempFile.toFile());
//                tempFiles.add(tempFile);
//            }
//
//            // 执行上传
//            String result = OkHttpStaticUtil.uploadMultipleFiles(url, fileMap, formData, headers);
//
//            // 清理临时文件
//            tempFiles.forEach(path -> {
//                try {
//                    Files.deleteIfExists(path);
//                } catch (IOException e) {
//                    log.warn("删除临时文件失败: {}", path, e);
//                }
//            });
//
//            return buildSuccessResponse("多文件上传成功", result);
//        } catch (Exception e) {
//            log.error("多文件上传失败", e);
//            return buildErrorResponse("多文件上传失败", e.getMessage());
//        }
//    }
//
//    // ==================== 文件下载测试 ====================
//
//    /**
//     * 文件下载测试（返回字节流）
//     */
//    @GetMapping("/test-download")
//    public void testDownload(@RequestParam String url,
//                             @RequestHeader(required = false) Map<String, String> headers,
//                             HttpServletResponse response) {
//        try {
//            log.info("测试文件下载: {}", url);
//
//            // 下载文件为字节数组
//            byte[] fileBytes = OkHttpStaticUtil.downloadFileAsBytes(url, headers);
//
//            // 设置响应头
//            response.setContentType("application/octet-stream");
//            response.setHeader("Content-Disposition", "attachment; filename=downloaded_file");
//            response.setContentLength(fileBytes.length);
//
//            // 写入响应流
//            try (OutputStream os = response.getOutputStream()) {
//                os.write(fileBytes);
//                os.flush();
//            }
//
//        } catch (Exception e) {
//            log.error("文件下载失败", e);
//            response.setStatus(500);
//            try {
//                response.getWriter().write("文件下载失败: " + e.getMessage());
//            } catch (IOException ex) {
//                log.error("写入错误响应失败", ex);
//            }
//        }
//    }
//
//    /**
//     * 文件下载测试（保存到服务器指定路径）
//     */
//    @GetMapping("/test-download-to-path")
//    public String testDownloadToPath(@RequestParam String url,
//                                     @RequestParam String savePath,
//                                     @RequestParam(required = false) String fileName,
//                                     @RequestHeader(required = false) Map<String, String> headers) {
//        try {
//            log.info("测试文件下载到路径: {}, 保存路径: {}", url, savePath);
//
//            // 创建保存目录
//            Path saveDir = Paths.get(savePath);
//            if (!Files.exists(saveDir)) {
//                Files.createDirectories(saveDir);
//            }
//
//            // 执行下载
//            OkHttpStaticUtil.downloadFile(url, savePath, fileName, headers);
//
//            return buildSuccessResponse("文件下载成功", "文件已保存到: " + savePath);
//        } catch (Exception e) {
//            log.error("文件下载到路径失败", e);
//            return buildErrorResponse("文件下载到路径失败", e.getMessage());
//        }
//    }
//
//    // ==================== 异步请求测试 ====================
//
//    /**
//     * 异步GET请求测试
//     */
//    @GetMapping("/test-async-get")
//    public String testAsyncGet(@RequestParam String url,
//                               @RequestHeader(required = false) Map<String, String> headers) {
//        try {
//            log.info("测试异步GET请求: {}", url);
//            String requestId = UUID.randomUUID().toString();
//
//            CompletableFuture.runAsync(() -> {
//                OkHttpStaticUtil.doGetAsync(url, headers, new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        asyncResults.put(requestId, "失败: " + e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        String result = response.isSuccessful()
//                                ? "成功: " + (response.body() != null ? response.body().string() : "")
//                                : "失败: HTTP " + response.code();
//                        asyncResults.put(requestId, result);
//                        response.close();
//                    }
//                });
//            });
//
//            return buildSuccessResponse("异步GET请求已提交",
//                    "请求ID: " + requestId + "，稍后通过 /api/okhttp-test/async-result/" + requestId + " 查询结果");
//        } catch (Exception e) {
//            log.error("异步GET请求失败", e);
//            return buildErrorResponse("异步GET请求失败", e.getMessage());
//        }
//    }
//
//    /**
//     * 异步POST请求测试
//     */
//    @PostMapping("/test-async-post")
//    public String testAsyncPost(@RequestParam String url,
//                                @RequestBody String jsonBody,
//                                @RequestHeader(required = false) Map<String, String> headers) {
//        try {
//            log.info("测试异步POST请求: {}, 请求体长度: {}", url, jsonBody.length());
//            String requestId = UUID.randomUUID().toString();
//
//            CompletableFuture.runAsync(() -> {
//                OkHttpStaticUtil.doPostJsonAsync(url, jsonBody, headers, new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        asyncResults.put(requestId, "失败: " + e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        String result = response.isSuccessful()
//                                ? "成功: " + (response.body() != null ? response.body().string() : "")
//                                : "失败: HTTP " + response.code();
//                        asyncResults.put(requestId, result);
//                        response.close();
//                    }
//                });
//            });
//
//            return buildSuccessResponse("异步POST请求已提交",
//                    "请求ID: " + requestId + "，稍后通过 /api/okhttp-test/async-result/" + requestId + " 查询结果");
//        } catch (Exception e) {
//            log.error("异步POST请求失败", e);
//            return buildErrorResponse("异步POST请求失败", e.getMessage());
//        }
//    }
//
//    /**
//     * 查询异步请求结果
//     */
//    @GetMapping("/async-result/{requestId}")
//    public String getAsyncResult(@PathVariable String requestId) {
//        String result = asyncResults.get(requestId);
//        if (result != null) {
//            asyncResults.remove(requestId); // 查询后移除
//            return buildSuccessResponse("异步请求结果", result);
//        } else {
//            return buildErrorResponse("未找到结果", "请求ID: " + requestId + " 的结果不存在或已过期");
//        }
//    }
//
//    // ==================== 连接池状态查询 ====================
//
//    /**
//     * 查询OkHttp连接池状态
//     */
//    @GetMapping("/pool-status")
//    public String getPoolStatus() {
//        try {
//            String stats = OkHttpStaticUtil.getPoolStats();
//            return buildSuccessResponse("连接池状态", stats);
//        } catch (Exception e) {
//            log.error("查询连接池状态失败", e);
//            return buildErrorResponse("查询连接池状态失败", e.getMessage());
//        }
//    }
//
//    // ==================== 综合测试 ====================
//
//    /**
//     * 综合测试：模拟完整的API调用流程
//     */
//    @PostMapping("/comprehensive-test")
//    public String comprehensiveTest(@RequestParam String baseUrl) {
//        Map<String, Object> results = new LinkedHashMap<>();
//
//        try {
//            // 1. GET请求测试
//            String getResult = OkHttpStaticUtil.doGet(baseUrl + "/api/test/get");
//            results.put("GET测试", getResult.length() > 100 ?
//                    getResult.substring(0, 100) + "..." : getResult);
//
//            // 2. POST JSON测试
//            Map<String, Object> jsonData = new HashMap<>();
//            jsonData.put("testId", "comprehensive_" + System.currentTimeMillis());
//            jsonData.put("message", "综合测试");
//            jsonData.put("timestamp", new Date());
//
//            String jsonBody = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(jsonData);
//            String postResult = OkHttpStaticUtil.doPostJson(baseUrl + "/api/test/post", jsonBody, null);
//            results.put("POST JSON测试", postResult.length() > 100 ?
//                    postResult.substring(0, 100) + "..." : postResult);
//
//            // 3. PUT测试
//            jsonData.put("updated", true);
//            String putBody = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(jsonData);
//            String putResult = OkHttpStaticUtil.doPutJson(baseUrl + "/api/test/put", putBody, null);
//            results.put("PUT测试", putResult.length() > 100 ?
//                    putResult.substring(0, 100) + "..." : putResult);
//
//            return buildSuccessResponse("综合测试完成", results);
//
//        } catch (Exception e) {
//            log.error("综合测试失败", e);
//            return buildErrorResponse("综合测试失败", e.getMessage());
//        }
//    }
//
//    // ==================== 辅助方法 ====================
//
//    private String buildSuccessResponse(String message, Object data) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("code", 200);
//        response.put("message", message);
//        response.put("data", data);
//        response.put("timestamp", System.currentTimeMillis());
//        return toJson(response);
//    }
//
//    private String buildErrorResponse(String message, String error) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", false);
//        response.put("code", 500);
//        response.put("message", message);
//        response.put("error", error);
//        response.put("timestamp", System.currentTimeMillis());
//        return toJson(response);
//    }
//
//    private String toJson(Map<String, Object> map) {
//        try {
//            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(map);
//        } catch (Exception e) {
//            return "{\"success\":false,\"error\":\"JSON序列化失败\"}";
//        }
//    }
//
//
//}
