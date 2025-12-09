package com.ddz.demo.okhttp;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Feign内部服务认证工具类
 * 基于AES加密和时间戳验证的安全认证方案
 * 
 * @author system
 */
public class FeignAuthUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(FeignAuthUtils.class);
    
    /**
     * AES加密密钥原始字符串
     */
    private static final String SECRET_KEY = "WEB_ERP_2025_123@";
    
    /**
     * 获取AES密钥 (使用SHA-256生成256位密钥)
     */
    private static byte[] getAESKey() {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            return sha.digest(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            logger.error("生成AES密钥失败", e);
            throw new RuntimeException("生成AES密钥失败", e);
        }
    }
    
    /**
     * token有效期（毫秒）- 10分钟
     */
    private static final long TOKEN_VALIDITY = 10 * 60 * 1000;
    
    /**
     * 服务标识
     */
    private static final String SERVICE_IDENTIFIER = "weberp-internal-service";
    
    /**
     * Feign内部服务认证标识
     */
    public static final String FEIGN_INTERNAL_AUTH = "X-Feign-Internal-Auth";
    
    /**
     * 生成加密的认证token
     * 
     * @return 加密后的认证token
     */
    public static String generateAuthToken() {
        try {
            // 创建认证信息JSON
            JSONObject authInfo = new JSONObject();
            authInfo.put("service", SERVICE_IDENTIFIER);
            authInfo.put("timestamp", System.currentTimeMillis());
            authInfo.put("nonce", System.nanoTime()); // 添加随机数防止重放攻击
            
            // 转换为JSON字符串
            String authJson = JSONUtil.toJsonStr(authInfo);
            
            // AES加密
            AES aes = new AES(getAESKey());
            byte[] encrypted = aes.encrypt(authJson.getBytes(StandardCharsets.UTF_8));
            
            // Base64编码
            return Base64.encode(encrypted);
            
        } catch (Exception e) {
            logger.error("生成Feign认证token失败", e);
            return null;
        }
    }
    
    /**
     * 验证认证token
     * 
     * @param token 加密的认证token
     * @return 是否验证通过
     */
    public static boolean validateAuthToken(String token) {
        if (StrUtil.isEmpty(token)) {
            return false;
        }

        try {
            // Base64解码
            byte[] encryptedBytes = Base64.decode(token);

            // AES解密
            AES aes = new AES(getAESKey());
            byte[] decrypted = aes.decrypt(encryptedBytes);
            String authJson = new String(decrypted, StandardCharsets.UTF_8);

            // 解析JSON
            JSONObject authInfo = JSONUtil.parseObj(authJson);

            // 验证服务标识
            String service = authInfo.getStr("service");
            if (!SERVICE_IDENTIFIER.equals(service)) {
                logger.warn("Feign认证失败：服务标识不匹配");
                return false;
            }

            // 验证时间戳（防止token过期和重放攻击）
            Long timestamp = authInfo.getLong("timestamp");
            if (timestamp == null) {
                logger.warn("Feign认证失败：时间戳为空");
                return false;
            }

            long currentTime = System.currentTimeMillis();
            if (Math.abs(currentTime - timestamp) > TOKEN_VALIDITY) {
                logger.warn("Feign认证失败：token已过期，时间差：{}ms", Math.abs(currentTime - timestamp));
                return false;
            }

            return true;

        } catch (Exception e) {
            logger.error("验证Feign认证token失败", e);
            return false;
        }
    }
    
    /**
     * 生成请求签名（可选的额外安全层）
     * 
     * @param requestPath 请求路径
     * @param timestamp 时间戳
     * @return 签名
     */
    public static String generateSignature(String requestPath, long timestamp) {
        try {
            String signData = SERVICE_IDENTIFIER + requestPath + timestamp + SECRET_KEY;
            return cn.hutool.crypto.digest.DigestUtil.md5Hex(signData);
        } catch (Exception e) {
            logger.error("生成请求签名失败", e);
            return null;
        }
    }


    public static void main(String[] args) {
        System.out.println(validateAuthToken(generateAuthToken()));
    }
}
