package com.ddz.core.validate.validator;

import com.ddz.core.validate.annotation.ValidMobile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MobileValidator implements ConstraintValidator<ValidMobile, String> {
    @Override
    public void initialize(ValidMobile constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
