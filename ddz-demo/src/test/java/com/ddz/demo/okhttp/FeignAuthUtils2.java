package com.ddz.demo.okhttp;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


public class FeignAuthUtils2 {
    public static final String SECRET = "all-admin-secret-1234567890!@#$%^&*()QWERTYUIOPASDFGHJKLZXCVBNM";

//    @PostMapping
//    public R<String> login(@RequestBody LoginReq loginReq) {
//        String hrStaffId = Base64Decoder.decodeStr(loginReq.getZiy());
//
//        R<StaffInfoDto> r = platformQualityFeignClient.findForAuditLogin(hrStaffId);
//        if (!Objects.equals(r.getCode(), 200)) {
//            throw new RuntimeException("登录验证失败");
//        }
//        if (r.getData() == null) {
//            throw new RuntimeException("该ziy不存在");
//        }
//
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("sub", hrStaffId);
//        claims.put("iat", new Date());
//
//        Base64.Encoder encoder = Base64.getEncoder();
//        String encode = encoder.encodeToString(SECRET.getBytes());
//        SecretKey secretKey = Keys.hmacShaKeyFor(encode.getBytes());
//        String token = Jwts.builder()
//                .setClaims(claims)
//                .setExpiration(this.generateExpirationDate())
//                .signWith(secretKey, SignatureAlgorithm.HS256)
//                .compact();
//
//        PermissionDto permissionDto = new PermissionDto();
//        permissionDto.setHrStaffId(hrStaffId);
//        permissionDto.setStaffId(r.getData().getStaffId());
//        redisUtils.set("cloud_erp_per:" + token, JSON.toJSONString(permissionDto));
//        redisUtils.expire("cloud_erp_per:" + token, 60, TimeUnit.DAYS);
//
//        return R.ok(token);
//    }


    public static void main(String[] args) {
//        System.out.println(generateAuthToken());
//        System.out.println(validateAuthToken(generateAuthToken()));
    }
}
