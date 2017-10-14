package com.td.dtos.constraints;

import com.td.daos.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserExistenceValidator implements ConstraintValidator<UserExistence, String> {

    private boolean isExist;

    @Autowired
    private UserDao userDao;

    @Override
    public void initialize(UserExistence constraint) {
        isExist = constraint.value();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return userDao.checkUser(email) == isExist;
    }

}
