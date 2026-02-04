package com.ddz.demo.okhttp;

import com.ddz.core.utils.ObjectMapperUtil;
import com.ddz.demo.okhttp.domain.UserDemoDTO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class OkHttpTests {

    // ========================= get  get  get  get  get  get  get  get  get  get  get  get  get  get  get =========================
    @Test
    public void getEasy() throws IOException {
        String url = "http://localhost:8099/ddz/demo/okhttp/get/easy";
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Test-Header-Param", "123456789");
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("id", "1234567");
        queryParams.put("code", "abcdefg");
        String response = OkHttpStaticUtil.get(url, headers, queryParams);
        System.out.println(response);
    }

    @Test
    public void getParams() throws IOException {
        String url = "http://localhost:8099/ddz/demo/okhttp/get/params";
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Test-Header-Param", "123456789");
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("id", "1234567");
        queryParams.put("code", "abcdefg");
        String response = OkHttpStaticUtil.get(url, headers, queryParams);
        System.out.println(response);
    }

    @Test
    public void getFull() throws IOException {
        String url = "http://localhost:8099/ddz/demo/okhttp/get/full/测试";
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Test-Header-Param", "123456789");
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("id", "1234567");
        queryParams.put("name", "abcdefg");
        String response = OkHttpStaticUtil.get(url, headers, queryParams);
        System.out.println(response);
    }

    // ========================= post  post  post  post  post  post  post  post  post  post  post  post  post  post  post =========================

    @Test
    public void postJson() throws IOException {
        List<UserDemoDTO> list = UserDemoDTO.buildUsers(3);
        // 将对象转换为json字符串
        String jsonStr = ObjectMapperUtil.JSON.writeValueAsString(list);
        String url = "http://localhost:8099/ddz/demo/okhttp/post/json";
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Test-Header-Param", "123456789");
        String response = OkHttpStaticUtil.postJson(url, jsonStr, headers);
        System.out.println(response);
    }

    @Test
    public void postJsonAsync() throws IOException, InterruptedException {
        UserDemoDTO dto = UserDemoDTO.buildUser();
        // 将对象转换为json字符串
        String jsonStr = ObjectMapperUtil.JSON.writeValueAsString(dto);
        String url = "http://localhost:8099/ddz/demo/okhttp/post/json/async";
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Test-Header-Param", "123456789");
        // 异步调用
        OkHttpStaticUtil.postJsonAsync(url, jsonStr, headers, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("============================ 失败回调 ============================");
                e.printStackTrace();
                System.out.println("============================ 失败回调 ============================");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    String responseBody = response.body() != null ? response.body().string() : "";
                    if (!response.isSuccessful()) {
                        throw new IOException("HTTP " + response.code() + ": " + responseBody);
                    }
                    System.out.println("============================ 成功回调 ============================");
                    System.out.println(responseBody);
                    System.out.println("============================ 成功回调 ============================");
                } finally {
                    // 释放资源
                    response.close();
                }
            }
        });
        // TODO 异步请求中，测试类需要阻塞一下，否则主线程直接关闭就结束了，甚至真实的请求还没发起就被干掉了线程资源
        Thread.sleep(5000);
        System.out.println("============================ 异步请求主线程结束 ============================");
    }

    @Test
    public void postXml() throws IOException {
        UserDemoDTO dto = UserDemoDTO.buildUser();
        // 将对象转换为xml字符串
        String xmlStr = ObjectMapperUtil.XML.writeValueAsString(dto);
        String url = "http://localhost:8099/ddz/demo/okhttp/post/xml";
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Test-Header-Param", "123456789");
        String response = OkHttpStaticUtil.postXml(url, xmlStr, headers);
        System.out.println(response);
    }


    @Test
    public void postForm() throws IOException {
        String url = "http://localhost:8099/ddz/demo/okhttp/post/form";
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Test-Header-Param", "123456789");
        Map<String, String> formData = new HashMap<>();
        formData.put("id", "1234567");
        formData.put("code", "abcdefg");
        String response = OkHttpStaticUtil.postForm(url, formData, headers);
        System.out.println(response);
    }


    @Test
    public void postText() throws IOException {
        String url = "http://localhost:8099/ddz/demo/okhttp/post/text";
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Test-Header-Param", "123456789");
        String text = "测试 text 报文";
        String response = OkHttpStaticUtil.postText(url, text, headers);
        System.out.println(response);
    }

    // ========================= put  put  put  put  put  put  put  put  put  put  put  put  put  put  put =========================

    @Test
    public void putJson() throws IOException {
        UserDemoDTO dto = UserDemoDTO.buildUser();
        // 将对象转换为json字符串
        String jsonStr = ObjectMapperUtil.JSON.writeValueAsString(dto);
        String url = "http://localhost:8099/ddz/demo/okhttp/put/json";
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Test-Header-Param", "123456789");
        String response = OkHttpStaticUtil.putJson(url, jsonStr, headers);
        System.out.println(response);
    }

    // ========================= delete  delete  delete  delete  delete  delete  delete  delete  delete  delete =========================

    @Test
    public void deleteJson() throws IOException {
        UserDemoDTO dto = UserDemoDTO.buildUser();
        // 将对象转换为json字符串
        String jsonStr = ObjectMapperUtil.JSON.writeValueAsString(dto);
        String url = "http://localhost:8099/ddz/demo/okhttp/delete/json";
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Test-Header-Param", "123456789");
        String response = OkHttpStaticUtil.deleteJson(url, jsonStr, headers);
        System.out.println(response);
    }


    @Test
    public void uploadFile() throws IOException {
        // 测试文件
        File file = new File("C:\\Users\\74066\\Desktop\\AAAAAA\\测试文件1.pdf");

        UserDemoDTO dto = UserDemoDTO.buildUser();
        // 将对象转换为json字符串
        String jsonStr = ObjectMapperUtil.JSON.writeValueAsString(dto);
        String url = "http://localhost:8099/ddz/demo/okhttp/uploadFile";
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Test-Header-Param", "123456789");
        headers.put("Content-Type", "multipart/form-data");
        Map<String, String> formData = new HashMap<>();
        formData.put("newFilePath", "C:\\Users\\74066\\Desktop\\BBBBBB\\");
        String response = OkHttpStaticUtil.uploadFile(url, file, "file", formData, headers);
        System.out.println(response);
    }








    @BeforeAll
    public static void before(){
        System.out.println("================================= 测试开始 =================================");
    }

    @AfterAll
    public static void after(){
        System.out.println("================================= 测试结束 =================================");
    }



}

