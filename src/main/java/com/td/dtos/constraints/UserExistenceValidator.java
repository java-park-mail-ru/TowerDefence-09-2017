package com.td.dtos.constraints;

import com.td.services.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserExistenceValidator implements ConstraintValidator<UserExistence, String> {
    private boolean isExist;

    @Override
    public void initialize(UserExistence constraint) {
        isExist = constraint.value();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return UserService.checkIfUserExists(email) == isExist;
    }

}
