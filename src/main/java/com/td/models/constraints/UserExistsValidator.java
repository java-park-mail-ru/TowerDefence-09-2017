package com.td.models.constraints;

import com.td.models.User;
import com.td.services.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserExistsValidator implements ConstraintValidator<UserExistsConstraint, String> {


    @Override
    public void initialize(UserExistsConstraint constraint) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context){
        return UserService.checkIfUserExists(email);
    }

}
