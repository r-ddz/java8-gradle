package com.ddz.demo.okhttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



/**
 * 使用示例
 *
 * @author ddz
 */
@Slf4j
@Service
public class DemoService {

    @Autowired
    private OkHttpUtil okHttpUtil;

    /**
     * 同步GET请求示例
     */
    public void syncGetExample() {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("User-Agent", "MyApp/1.0");

            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("page", "1");
            queryParams.put("size", "20");

            String response = okHttpUtil.doGet(
                    "https://api.example.com/data",
                    headers,
                    queryParams
            );

            log.info("GET响应: {}", response);
        } catch (IOException e) {
            log.error("请求失败", e);
        }
    }

    /**
     * 同步POST请求示例
     */
    public void syncPostExample() {
        try {
            String jsonBody = "{\"username\":\"test\", \"password\":\"123456\"}";

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer your_token");

            String response = okHttpUtil.doPostJson(
                    "https://api.example.com/login",
                    jsonBody,
                    headers
            );

            log.info("POST响应: {}", response);
        } catch (IOException e) {
            log.error("请求失败", e);
        }
    }

    /**
     * 异步请求示例
     */
    public void asyncPostExample() {
        String jsonBody = "{\"hrStaffId\":\"ZIY00106656\",\"year\":\"2025\"}";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        okHttpUtil.doPostJsonAsync(
                "https://jztweberp.dev.jztweb.com/basic-data/api/supplier/letter/querySupplierLetterAuditList",
                jsonBody,
                headers,
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        log.error("异步请求失败", e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseBody = response.body() != null ? response.body().string() : "";
                            log.info("异步请求成功: {}", responseBody);
                        } else {
                            log.error("异步请求失败，状态码: {}", response.code());
                        }
                        response.close();
                    }
                }
        );
    }

    /**
     * 监控连接池状态
     */
    public void monitorConnectionPool() {
        String stats = okHttpUtil.getConnectionPoolStats();
        log.info("连接池状态: {}", stats);
    }
}
