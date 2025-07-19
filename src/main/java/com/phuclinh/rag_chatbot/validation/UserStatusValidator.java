package com.phuclinh.rag_chatbot.validation;

import com.phuclinh.rag_chatbot.enums.UserStatus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserStatusValidator implements ConstraintValidator<ValidUserStatus, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true; // null = không cập nhật

        try {
            UserStatus.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
