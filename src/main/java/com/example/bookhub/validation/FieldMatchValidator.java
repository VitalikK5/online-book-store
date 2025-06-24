package com.example.bookhub.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Objects;
import org.springframework.beans.BeanUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            PropertyDescriptor firstDescriptor =
                    BeanUtils.getPropertyDescriptor(
                            value.getClass(),
                            firstFieldName
                    );

            PropertyDescriptor secondDescriptor =
                    BeanUtils.getPropertyDescriptor(
                            value.getClass(),
                            secondFieldName
                    );

            if (firstDescriptor == null || secondDescriptor == null) {
                return false;
            }

            Method readFirst = firstDescriptor.getReadMethod();
            Method readSecond = secondDescriptor.getReadMethod();

            Object firstValue = readFirst.invoke(value);
            Object secondValue = readSecond.invoke(value);

            return Objects.equals(firstValue, secondValue);

        } catch (Exception e) {
            return false;
        }
    }
}
