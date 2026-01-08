package com.AOP.Practice.DTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail,String>{

    List<String> emails ;


    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        emials = List.of("email1@gmail.com",)
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
