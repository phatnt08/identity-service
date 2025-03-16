package com.ntp.identity_service.validator.Annotation.Age;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;

@Target({ ElementType.FIELD }) // The annotation can only be applied to fields
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME) // The annotation will be available at runtime
@Constraint(validatedBy = { AgeValidator.class }) // The class that will implement the validation logic
public @interface Age {

    String message() default "Invalid date of birth"; // The default error message

    int min(); // The minimum age allowed

    Class<?>[] groups() default {}; // The groups to which the constraint belongs

    Class<? extends jakarta.validation.Payload>[] payload() default {}; // The payload associated to the constraint

}
