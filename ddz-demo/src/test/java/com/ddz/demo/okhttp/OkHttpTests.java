package com.ddz.demo.okhttp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest(classes = OkHttpTestApplication.class)
public class OkHttpTests {

    @Autowired
    private OkHttpUtil okHttpUtil;

    @Test
    public void test() throws IOException {

        String jsonBody = "{\"hrStaffId\":\"ZIY00106656\",\"agreementunitName\":\"\",\"creatorName\":\"\",\"letterNo\":\"\",\"year\":\"2025\",\"cycleType\":\"\",\"cycleNum\":\"\",\"pageIndex\":1,\"pageSize\":200}";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Feign-Internal-Auth", FeignAuthUtils.generateAuthToken());
//        headers.put("Authorization", "Bearer your_token");

        String response = okHttpUtil.doPostJson(
                "https://jztweberp.dev.jztweb.com/auxiliary/api/supplier/letter/querySupplierLetterAuditList",
                jsonBody,
                headers
        );

        log.info("POST响应: {}", response);
    }


    @Test
    public void testToken() throws IOException {
        System.out.println("================================= 开始 =================================");
        System.out.println(FeignAuthUtils.generateAuthToken());
        System.out.println("================================= 结束 =================================");
    }



}

