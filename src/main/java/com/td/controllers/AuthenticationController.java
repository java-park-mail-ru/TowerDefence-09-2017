package com.td.controllers;

import com.td.models.ResponseStatus;
import com.td.models.SigninForm;
import com.td.models.User;
import com.td.models.groups.NewUser;
import com.td.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "https://tdteam.herokuapp.com",
        allowedHeaders = "Content-Type",
        allowCredentials = "true")
@RequestMapping(path = "/auth")
public class AuthenticationController {

    @PostMapping(path = "/signin", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity loginUser(@Valid @RequestBody SigninForm user, HttpSession httpSession) {

        if (httpSession.getAttribute(UserService.USER_SESSION_KEY) != null) {
            return new ResponseEntity<>(new ResponseStatus("User is already logged in"), HttpStatus.BAD_REQUEST);
        }

        final String password = user.getPassword();
        final String mail = user.getEmail();

        final User dbUser = UserService.getUser(mail);

        if (!BCrypt.checkpw(password, dbUser.getPassword())) {
            return new ResponseEntity<>(new ResponseStatus("Incorrect password"), HttpStatus.BAD_REQUEST);
        }

        httpSession.setAttribute(UserService.USER_SESSION_KEY, dbUser.getId());

        return new ResponseEntity<>(dbUser, HttpStatus.OK);
    }

    @PostMapping(path = "/signup", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity registerUser(@Validated(NewUser.class) @RequestBody User user, HttpSession httpSession) {

        if (httpSession.getAttribute(UserService.USER_SESSION_KEY) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseStatus("User is already logged in"));
        }

        user.savePassword();

        UserService.storeUser(user);
        httpSession.setAttribute(UserService.USER_SESSION_KEY, user.getId());

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping(path = "/logout", produces = "application/json")
    @ResponseBody
    public ResponseEntity logoutUserBySession(HttpSession httpSession) {
        if (httpSession.getAttribute(UserService.USER_SESSION_KEY) == null) {
            return new ResponseEntity<>(new ResponseStatus("Unauthorized"), HttpStatus.UNAUTHORIZED);
        }
        httpSession.invalidate();
        return new ResponseEntity<>(new ResponseStatus("Success"), HttpStatus.OK);

    }
}
