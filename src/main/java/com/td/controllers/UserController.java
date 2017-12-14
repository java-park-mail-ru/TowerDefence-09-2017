package com.td.controllers;

import com.td.Constants;
import com.td.daos.UserDao;
import com.td.domain.User;
import com.td.dtos.UserDto;
import com.td.dtos.groups.UpdateUser;
import com.td.exceptions.AuthException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    private final ModelMapper modelMapper;

    private final UserDao userDao;

    @Autowired
    public UserController(ModelMapper modelMapper, UserDao userDao) {
        this.modelMapper = modelMapper;
        this.userDao = userDao;
    }

    @GetMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity getUserBySession(HttpSession httpSession) {

        Long userId = (Long) httpSession.getAttribute(Constants.USER_SESSION_KEY);
        if (userId == null) {
            throw new AuthException("unauthorized", "/api/user", HttpStatus.UNAUTHORIZED);
        }
        User user = userDao.getUserById(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modelMapper.map(user, UserDto.class));
    }

    @PostMapping(path = "/edit", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity editUser(@Validated(UpdateUser.class) @RequestBody UserDto userDto, HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute(Constants.USER_SESSION_KEY);

        if (userId == null || +userDto.getId() != userId) {
            throw new AuthException("it's forbidden to edit other's data", "/edit", HttpStatus.FORBIDDEN);
        }
        User updated = userDao.updateUserById(userDto.getId(),
                userDto.getEmail(),
                userDto.getLogin(),
                userDto.getPassword());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modelMapper.map(updated, UserDto.class));
    }

}
