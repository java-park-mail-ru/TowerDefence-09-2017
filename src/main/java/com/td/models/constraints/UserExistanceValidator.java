package com.td.models.constraints;

import com.td.services.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserExistanceValidator implements ConstraintValidator<UserExistance, String> {
    private boolean isExist;

    @Override
    public void initialize(UserExistance constraint) {
        isExist = constraint.value();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return UserService.checkIfUserExists(email) == isExist;
    }

}
