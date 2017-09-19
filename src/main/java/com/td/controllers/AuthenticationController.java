package com.td.controllers;

import com.td.models.SigninForm;
import com.td.models.SignupForm;
import com.td.models.User;
import com.td.models.ResponseStatus;

import com.td.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@SuppressWarnings("unused")
@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

    @PostMapping(path = "/signin", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity loginUser(@Valid @RequestBody SigninForm user, HttpSession httpSession) {

        final UserService userService;

        if (httpSession.getAttribute("user") != null) {
            return new ResponseEntity<>(new ResponseStatus("User is already logged in"), HttpStatus.BAD_REQUEST);
        }

        final String password = user.getPassword();
        final String mail = user.getEmail();

        final User dbUser = UserService.getUser(mail);

        if (!BCrypt.checkpw(password, dbUser.getPassword())) {
            return new ResponseEntity<>(new ResponseStatus("Incorrect password"), HttpStatus.BAD_REQUEST);
        }

        httpSession.setAttribute("user", dbUser);

        return new ResponseEntity<>(new ResponseStatus("Success"), HttpStatus.OK);
    }

    @PostMapping(path = "/signup", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity registerUser(@Valid @RequestBody SignupForm user, HttpSession httpSession) {

        if (httpSession.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseStatus("User is already logged in"));
        }

        final String mail = user.getEmail();
        final String password = user.getPassword();
        final String login = user.getEmail();

        final User newUser = new User(user.getEmail(), user.getPassword(), user.getEmail());
        UserService.addUser(newUser);
        httpSession.setAttribute("user", newUser);

        return new ResponseEntity<>(new ResponseStatus("Success"), HttpStatus.OK);
    }

    @PostMapping(path = "/logout", produces = "application/json")
    @ResponseBody
    public ResponseEntity logoutUserBySession(HttpSession httpSession) {
        if (httpSession.getAttribute("user") == null) {
            return new ResponseEntity<>(new ResponseStatus("Unauthorized"), HttpStatus.UNAUTHORIZED);
        }
        httpSession.invalidate();
        return new ResponseEntity<>(new ResponseStatus("Session successfully invalidated"), HttpStatus.OK);

    }
}
