package com.ippl.difability.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.annotation.*;

import com.ippl.difability.enums.Role;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllowedRole.AllowedRoleValidator.class)
public @interface AllowedRole {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class AllowedRoleValidator implements ConstraintValidator<AllowedRole, Role> {
        @Override
        public boolean isValid(Role value, ConstraintValidatorContext context) {
            if (value == null) return false;
            return value == Role.COMPANY || value == Role.JOB_SEEKER;
        }
    }
}
