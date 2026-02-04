package com.ddz.core.validate.annotation;

import com.ddz.core.validate.validator.MobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义手机号校验注解
 *
 * @author ddz
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileValidator.class) // 指定校验器
public @interface ValidMobile {

    /**
     * 正则表达式
     */
    String regexp() default ".*";

    /**
     * 错误消息
     */
    String message() default "手机字符串格式不正确";

    /**
     * 分组
     */
    Class<?>[] groups() default {};

    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 正则表达式标志 (可选)
     */
    Pattern.Flag[] flags() default {};

}
