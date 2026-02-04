package com.ddz.core.email.domain.result;

/**
 * 文件下载/上传结果
 * 用途：文件相关操作响应
 */
public class FileResult extends ApiResult<String>{
    private String fileName;      // 文件名
    private String fileType;      // 文件类型
    private Long fileSize;        // 文件大小（字节）
    private String downloadUrl;   // 下载URL
    private String filePath;      // 文件路径
}
