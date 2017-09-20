package com.td.controllers;

import com.td.models.ResponseStatus;
import com.td.models.User;
import com.td.models.groups.UpdateUser;
import com.td.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/user")
@CrossOrigin(origins = "https://tdteam.herokuapp.com",
        allowedHeaders = "Content-Type",
        allowCredentials = "true")
public class UserConroller {

    @GetMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity getUserBySession(HttpSession httpSession) {

        Long userId = (Long) httpSession.getAttribute(UserService.USER_SESSION_KEY);
        if (userId == null) {
            return new ResponseEntity<>(new ResponseStatus("Unauthorozed"), HttpStatus.UNAUTHORIZED);
        }
        User user = UserService.getUser(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(path = "/edit", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity editUser(@Validated(UpdateUser.class) @RequestBody User user, HttpSession httpSession) {
        if (httpSession.getAttribute(UserService.USER_SESSION_KEY) != user.getId()) {
            return new ResponseEntity<>(new ResponseStatus("Forbidden"), HttpStatus.FORBIDDEN);
        }
        final User oldUser = UserService.getUser(user.getId());
        final Long id = user.getId();
        final String email = user.getEmail();
        final String login = user.getLogin();
        final String password = user.getPassword();

        if (id != 0) {
            oldUser.setId(id);
        }

        if (email != null && !email.equals("")) {
            oldUser.setEmail(email);
        }

        if (login != null && !login.equals("")) {
            oldUser.setLogin(login);
        }
        if (password != null && !password.equals("")) {
            oldUser.setPassword(password);
        }
        return new ResponseEntity<>(new ResponseStatus("Updated"), HttpStatus.OK);
    }


}
