package com.td.controllers;

import com.sun.org.apache.regexp.internal.RE;
//import com.td.EmailValidator;
import com.td.models.SigninForm;
import com.td.models.User;
import com.td.models.ResponseStatus;

import com.td.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@SuppressWarnings("unused")
@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity loginUser(@Valid @RequestBody SigninForm user, HttpSession httpSession) {

        final UserService userService;

        if (httpSession.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseStatus("User is already logged in"));
        }

        final String password = user.getPassword();
        final String mail = user.getEmail();

        final User dbUser = UserService.getUser(mail);

        if (!BCrypt.checkpw(password, dbUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseStatus("Incorrect password"));
        }

        httpSession.setAttribute("user", dbUser);

        return new ResponseEntity<>(new ResponseStatus("Success").getStatus(), HttpStatus.OK);
    }

    @PostMapping(path = "/logout", produces = "application/json")
    @ResponseBody
    public ResponseEntity logoutUserBySession(HttpSession httpSession) {

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
