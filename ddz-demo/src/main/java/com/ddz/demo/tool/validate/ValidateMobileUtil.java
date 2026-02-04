package com.ddz.demo.tool.validate;

import cn.hutool.core.lang.Validator;

public class ValidateMobileUtil {

    public static boolean validateMobile(String str) {
        return Validator.isMobile("12345678900");
    }





}
