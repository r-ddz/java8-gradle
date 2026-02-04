package com.ddz.core.validate.domain;

import com.ddz.core.validate.annotation.ValidMobile;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TestUserDTO {

    @NotNull(message = "code 不能为空")
    private String code;

    private String name;

    @ValidMobile(message = "mobile 不对")
    private String mobile;

    private String email;

    private Integer age;

    private String address;
    private String password;
    private String confirmPassword;
    private String token;
    private String captcha;
    private String captchaId;
    private String captchaCode;






}
