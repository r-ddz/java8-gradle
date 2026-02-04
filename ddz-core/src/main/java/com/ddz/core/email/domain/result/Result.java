package com.ddz.core.email.domain.result;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private boolean success;
    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> ok() {
        return new Result<>(true, 200, "success", null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(true, 200, "success", data);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(false, 500, msg, null);
    }

    public static <T> Result<T> error(Integer code, String msg) {
        return new Result<>(false, code, msg, null);
    }

    public boolean isError() {
        return !isSuccess();
    }

}
