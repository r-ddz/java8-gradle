package com.ddz.demo.call.jzt;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SignUtil {

    public static String sign(String appKey, String secretKey, int nonce, long timestamp, String SHA) throws NoSuchAlgorithmException, InvalidKeyException {
        String key = appKey + nonce + timestamp;
        Mac sha256HMAC = Mac.getInstance(SHA);
        sha256HMAC.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SHA));
        // 对二进制密文进行base64,得到sign
        byte[] bytes = sha256HMAC.doFinal(key.getBytes());
        Base64.Encoder encoder = Base64.getEncoder();
        String sign = encoder.encodeToString(bytes);
        return sign;
    }





}
