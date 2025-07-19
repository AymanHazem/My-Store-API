package com.ayman.my_store_api.users;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LowercaseValidator implements ConstraintValidator<Lowercase,String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null==value) return true;
        return value.equals(value.toLowerCase());
    }
}
