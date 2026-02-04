package com.ddz.demo.okhttp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//@Slf4j
//@SpringBootTest(classes = OkHttpTestApplication.class)
public class OkHttpStaticTests {

    @Test
    public void test() {
        System.out.println("================================= 测试开始 =================================");
    }

    @Test
    public void test2() throws IOException {

        String jsonBody = "{\"hrStaffId\":\"ZIY00106656\",\"agreementunitName\":\"\",\"creatorName\":\"\",\"letterNo\":\"\",\"year\":\"2025\",\"cycleType\":\"\",\"cycleNum\":\"\",\"pageIndex\":1,\"pageSize\":200}";

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Feign-Internal-Auth", FeignAuthUtils.generateAuthToken());
        String response = OkHttpStaticUtil.postJson(
                "https://jztweberp.dev.jztweb.com/auxiliary/api123/supplier/letter/querySupplierLetterAuditList",
                jsonBody,
                headers
        );

        System.out.println("================================= 测试开始 =================================");
        System.out.println(response);
        System.out.println("================================= 测试结束 =================================");
    }

    @Test
    public void test3() throws IOException {

        String jsonBody = "{\"hrStaffId\":\"ZIY00106656\",\"agreementunitName\":\"\",\"creatorName\":\"\",\"letterNo\":\"\",\"year\":\"2025\",\"cycleType\":\"\",\"cycleNum\":\"\",\"pageIndex\":1,\"pageSize\":200}";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Feign-Internal-Auth", FeignAuthUtils.generateAuthToken());
//        headers.put("Authorization", "Bearer your_token");

//        String response = okHttpUtil.doPostJson(
//                "https://jztweberp.dev.jztweb.com/auxiliary/api/supplier/letter/querySupplierLetterAuditList",
//                jsonBody,
//                headers
//        );

//        log.info("POST响应: {}", response);
    }


}

