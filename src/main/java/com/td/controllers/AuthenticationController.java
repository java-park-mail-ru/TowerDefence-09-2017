package com.td.controllers;

import com.td.dtos.SigninFormDto;
import com.td.dtos.UserDto;
import com.td.dtos.groups.NewUser;
import com.td.models.ResponseStatus;
import com.td.models.User;
import com.td.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/auth")
@CrossOrigin(origins = "http://localhost:8000",
        allowedHeaders = "Content-Type",
        allowCredentials = "true")
public class AuthenticationController {
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(path = "/signin", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity loginUser(@Valid @RequestBody SigninFormDto user, HttpSession httpSession) {

        if (httpSession.getAttribute(UserService.USER_SESSION_KEY) != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseStatus("User is already logged in"));
        }

        final String password = user.getPassword();

        final User dbUser = UserService.getUser(user.getEmail());

        if (!dbUser.checkPassword(password)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseStatus("Incorrect password"));
        }

        httpSession.setAttribute(UserService.USER_SESSION_KEY, dbUser.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modelMapper.map(dbUser, UserDto.class));
    }

    @PostMapping(path = "/signup", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity registerUser(@Validated(NewUser.class) @RequestBody UserDto userDto, HttpSession httpSession) {
        User user = modelMapper.map(userDto, User.class);
        if (httpSession.getAttribute(UserService.USER_SESSION_KEY) != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseStatus("User is already logged in"));
        }

        UserService.storeUser(user);
        httpSession.setAttribute(UserService.USER_SESSION_KEY, user.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(modelMapper.map(user, UserDto.class));
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
