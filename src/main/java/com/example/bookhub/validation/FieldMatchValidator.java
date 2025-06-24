package com.example.bookhub.validation;

import com.example.bookhub.dto.user.UserRegistrationRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, UserRegistrationRequestDto> {
        @Override
        public boolean isValid(UserRegistrationRequestDto dto, ConstraintValidatorContext context) {
            if (dto.getPassword() == null || dto.getRepeatPassword() == null) {
                return false;
            }
            return dto.getPassword().equals(dto.getRepeatPassword());
        }
}
