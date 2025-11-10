package com.ddz.core.email.domain.Response;

import lombok.Data;

@Data
public class EmailResponse<T> {

    /** 是否成功 */
    private boolean success;
    /** 消息 */
    private String msg;
    /** 数据 */
    private T data;

    public EmailResponse(boolean success, String msg, T data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public static <T> EmailResponse<T> success() {
        return success(null);
    }

    public static <T> EmailResponse<T> success(String msg) {
        return success(msg, null);
    }

    public static <T> EmailResponse<T> success(String msg, T data) {
        return new EmailResponse<>(true, msg, data);
    }

    public static <T> EmailResponse<T> error() {
        return error("error", null);
    }

    public static <T> EmailResponse<T> error(String msg) {
        return error(msg, null);
    }

    public static <T> EmailResponse<T> error(String msg, T data) {
        return new EmailResponse<>(false, msg, data);
    }

}
