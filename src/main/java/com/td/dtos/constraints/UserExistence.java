package com.td.dtos.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserExistenceValidator.class)
public @interface UserExistence {
    String message() default "User existance check failed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean value();
}
