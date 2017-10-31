package com.td.dtos.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidatior implements ConstraintValidator<PasswordValid, String> {
    private int min;
    private int max;

    @Override
    public void initialize(PasswordValid constraint) {
        this.min = constraint.min();
        this.max = constraint.max();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return true;
        }
        return password.length() >= min && password.length() <= max;
    }
}
