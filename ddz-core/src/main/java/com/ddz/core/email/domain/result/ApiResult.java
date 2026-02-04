package com.ddz.core.email.domain.result;

/**
 * API响应对象，适用于Controller层返回给前端
 * 用途：HTTP接口响应，包含HTTP状态码和请求追踪
 */
public class ApiResult<T>  extends Result<T> {

    private Long timestamp;          // 响应时间戳
    private String requestId;        // 请求ID（用于链路追踪）
    private String path;             // 请求路径（可选）
    private String version;          // API版本（可选）


}
