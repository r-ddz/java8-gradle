package com.ddz.demo.call.jzt;

import cn.hutool.core.util.RandomUtil;
import com.ddz.demo.okhttp.OkHttpStaticUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.ItemsInventoryGetRequest;
import com.taobao.api.response.ItemsInventoryGetResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Call263Tests {

    @Test
    public void test() throws NoSuchAlgorithmException, InvalidKeyException, IOException {

        String appkey = "113999";
        String secretKey = "mqLx9i7lTw6mGB4Sv2KRtJynehOtiYuP";
        int nonce = RandomUtil.randomInt();
        long timestamp = System.currentTimeMillis() / 1000;
        String SHA = "HmacSHA256";
        String sign = SignUtil.sign(appkey, secretKey, nonce, timestamp, SHA);


//        String url = "https://demo.link263.cc/gateway/" + "/sys/openapi/employee/pageSearch";
        String url = "https://demo.link263.cc/gateway/" + "/call/openapi/cdrns/pageSearch4CallHistory";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.put("appkey", appkey);
        headers.put("nonce", nonce + "");
        headers.put("timestamp", timestamp + "");
        headers.put("sign", sign);


        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "1");
        queryParams.put("limit", "100");
        String response = OkHttpStaticUtil.get(url, headers, queryParams);
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

    @Test
    public void test1222222() throws ApiException {

        TaobaoClient client = new DefaultTaobaoClient(null, null, null);
        ItemsInventoryGetRequest req = new ItemsInventoryGetRequest();

        ItemsInventoryGetResponse rsp = client.execute(req, null);






    }

}
