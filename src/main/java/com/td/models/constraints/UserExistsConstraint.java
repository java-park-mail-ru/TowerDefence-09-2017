package com.td.models.constraints;

import com.td.models.User;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserExistsValidator.class)
public @interface UserExistsConstraint {
    String message() default "Email does not correspond to any user";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
