package com.td.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.td.Constants;
import com.td.daos.UserDao;
import com.td.domain.GameProfile;
import com.td.domain.User;
import com.td.dtos.ResponseJson;
import com.td.dtos.SigninFormDto;
import com.td.dtos.UserDto;
import com.td.dtos.errors.ErrorMessage;
import com.td.dtos.errors.ErrorTypes;
import com.td.dtos.errors.IncorrectRequestDataError;
import com.td.dtos.errors.views.ErrorViews;
import com.td.dtos.groups.NewUser;
import com.td.exceptions.AuthException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthenticationController {
    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final ModelMapper modelMapper;

    private final UserDao userDao;

    @Autowired
    public AuthenticationController(ModelMapper modelMapper, UserDao userDao) {
        this.modelMapper = modelMapper;
        this.userDao = userDao;
    }

    @PostMapping(path = "/signin", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity loginUser(@Valid @RequestBody SigninFormDto user, HttpSession httpSession) {

        if (httpSession.getAttribute(Constants.USER_SESSION_KEY) != null) {
            throw new AuthException("user is logged in already", "/signin", HttpStatus.CONFLICT);
        }

        final String password = user.getPassword();

        final User dbUser = userDao.getUserByEmail(user.getEmail());

        if (!dbUser.checkPassword(password)) {
            throw new AuthException("invalid password", "/signin", HttpStatus.BAD_REQUEST);
        }

        httpSession.setAttribute(Constants.USER_SESSION_KEY, dbUser.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modelMapper.map(dbUser, UserDto.class));
    }

    @PostMapping(path = "/signup", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity registerUser(@Validated(NewUser.class) @RequestBody UserDto userDto, HttpSession httpSession) {

        User user = modelMapper.map(userDto, User.class);
        if (httpSession.getAttribute(Constants.USER_SESSION_KEY) != null) {
            throw new AuthException("user is logged in already", "/signup", HttpStatus.CONFLICT);
        }

        user.setProfile(new GameProfile(userDto.getGameClass()));
        userDao.storeUser(user);
        httpSession.setAttribute(Constants.USER_SESSION_KEY, user.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modelMapper.map(user, UserDto.class));
    }

    @PostMapping(path = "/logout", produces = "application/json")
    @ResponseBody
    public ResponseEntity logoutUserBySession(HttpSession httpSession) {
        if (httpSession.getAttribute(Constants.USER_SESSION_KEY) == null) {
            throw new AuthException("unauthorized", "/logout", HttpStatus.UNAUTHORIZED);
        }
        httpSession.invalidate();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseJson("Success"));

    }

    @ExceptionHandler
    @JsonView(ErrorViews.IncorrectRequestData.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMethodArgumentNotValid(MethodArgumentNotValidException exception) throws JsonProcessingException {

        List<IncorrectRequestDataError> errors = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> {
                    log.error("Field {}: {} {}", fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
                    return new IncorrectRequestDataError(fieldError.getField(), fieldError.getDefaultMessage());
                })
                .collect(Collectors.toList());

        log.error("Invalid object comes to controller, {} ", exception);
        return ErrorMessage
                .builder()
                .setType(ErrorTypes.INCORRECT_REQUEST_DATA)
                .setIncorrectRequestDataErrors(errors)
                .build();
    }
}
