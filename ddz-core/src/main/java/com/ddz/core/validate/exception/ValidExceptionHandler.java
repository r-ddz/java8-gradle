package com.ddz.core.validate.exception;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@RestControllerAdvice
public class ValidExceptionHandler {



    // 处理 Service 层方法校验异常
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(violation ->
                        violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
        String message = "方法参数校验失败：" + errors;
        return message;
    }

}
