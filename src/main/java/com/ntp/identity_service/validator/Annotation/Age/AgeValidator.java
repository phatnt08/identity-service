package com.ntp.identity_service.validator.Annotation.Age;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AgeValidator implements ConstraintValidator<Age, LocalDate> {

    private int min;

    @Override
    public void initialize(Age constraintAnnotation) {
        this.min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext context) {
        if (dateOfBirth == null) {
            return true; // null values are considered valid
        }
        return LocalDate.now().minusYears(min).isAfter(dateOfBirth);
    }

}
