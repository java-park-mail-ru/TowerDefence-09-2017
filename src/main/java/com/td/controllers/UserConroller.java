package com.td.controllers;

import com.td.models.ResponseStatus;
import com.td.models.User;
import com.td.models.UserUpdate;
import com.td.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/user")
@CrossOrigin(origins = "https://tdteam.herokuapp.com",
        allowedHeaders = "Content-Type",
        allowCredentials = "true")
public class UserConroller {

    @GetMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity getUserBySession(HttpSession httpSession) {

        Long userId = (Long) httpSession.getAttribute("user");
        if (userId == null) {
            return new ResponseEntity<>(new ResponseStatus("Unauthorozed"), HttpStatus.UNAUTHORIZED);
        }
        User user = UserService.getUser(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(path = "/edit", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity editUser(@Valid @RequestBody UserUpdate user, HttpSession httpSession) {
        if (httpSession.getAttribute("user") != user.getId()) {
            return new ResponseEntity<>(new ResponseStatus("Not Allowed"), HttpStatus.METHOD_NOT_ALLOWED);
        }
        User oldUser = UserService.getUser(user.getId());
        Long id = user.getId();
        String email = user.getEmail();
        String login = user.getLogin();
        String password = user.getPassword();

        if (id != 0) {
            UserService.removeUser(id);
            oldUser.setId(id);
        }

        if (email != null && !email.equals("")) {
            UserService.removeUser(email);
            oldUser.setEmail(email);
        }

        if (login != null && !login.equals("")) {
            oldUser.setLogin(login);
        }
        if (password != null && !password.equals("")){
            oldUser.setPassword(password);
        }

        UserService.updateUser(oldUser);

        return new ResponseEntity<>(new ResponseStatus("Updated"), HttpStatus.OK);
    }


}
