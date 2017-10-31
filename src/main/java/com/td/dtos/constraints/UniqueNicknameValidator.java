package com.td.dtos.constraints;

import com.td.daos.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueNicknameValidator implements ConstraintValidator<UniqueNickname, String> {
   @Autowired
   private UserDao userDao;

   public void initialize(UniqueNickname constraint) {

   }

   public boolean isValid(String nickname, ConstraintValidatorContext context) {
      return !userDao.checkUserByNickname(nickname);
   }
}
