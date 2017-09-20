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
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseStatus("User is already logged in"));
        }

        final String password = user.getPassword();

        final User dbUser = UserService.getUser(user.getEmail());

        if (!BCrypt.checkpw(password, dbUser.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseStatus("Incorrect password"));
        }

        httpSession.setAttribute(UserService.USER_SESSION_KEY, dbUser.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dbUser);
    }

    @PostMapping(path = "/signup", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity registerUser(@Validated(NewUser.class) @RequestBody User user, HttpSession httpSession) {

        if (httpSession.getAttribute(UserService.USER_SESSION_KEY) != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseStatus("User is already logged in"));
        }

        UserService.storeUser(user);
        httpSession.setAttribute(UserService.USER_SESSION_KEY, user.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    @PostMapping(path = "/logout", produces = "application/json")
    @ResponseBody
    public ResponseEntity logoutUserBySession(HttpSession httpSession) {
        if (httpSession.getAttribute(UserService.USER_SESSION_KEY) == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseStatus("Unauthorized"));
        }
        httpSession.invalidate();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseStatus("Success"));

    }
}
