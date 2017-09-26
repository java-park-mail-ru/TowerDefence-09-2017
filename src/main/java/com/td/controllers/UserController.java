package com.td.controllers;

import com.td.dtos.UserDto;
import com.td.dtos.groups.UpdateUser;
import com.td.exceptions.AuthException;
import com.td.models.User;
import com.td.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity getUserBySession(HttpSession httpSession) {

        Long userId = (Long) httpSession.getAttribute(UserService.USER_SESSION_KEY);
        if (userId == null) {
            throw new AuthException("unauthorized", "/user", HttpStatus.UNAUTHORIZED);
        }
        User user = UserService.getUser(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modelMapper.map(user, UserDto.class));
    }

    @PostMapping(path = "/edit", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity editUser(@Validated(UpdateUser.class) @RequestBody UserDto userDto, HttpSession httpSession) {
        if (httpSession.getAttribute(UserService.USER_SESSION_KEY) != userDto.getId()) {
            throw new AuthException("it's forbidden to edit other's data", "/edit", HttpStatus.FORBIDDEN);
        }
        final User oldUser = UserService.getUser(userDto.getId());
        final Long id = userDto.getId();
        final String email = userDto.getEmail();
        final String login = userDto.getLogin();
        final String password = userDto.getPassword();

        if (id != 0) {
            oldUser.setId(id);
        }

        if (email != null && !email.equals("")) {
            UserService.removeUser(oldUser.getEmail());
            oldUser.setEmail(email);
            UserService.updateUser(oldUser);
        }

        if (login != null && !login.equals("")) {
            oldUser.setLogin(login);
        }
        if (password != null && !password.equals("")) {
            oldUser.setPassword(password);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modelMapper.map(oldUser, UserDto.class));
    }

}
