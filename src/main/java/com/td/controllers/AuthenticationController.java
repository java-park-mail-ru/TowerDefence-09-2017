package com.td.controllers;

import com.td.models.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@RequestMapping(path = "/login")
public class AuthenticationController {
    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity loginUser(@RequestBody User user, HttpSession httpSession) {


        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/logout", produces = "application/json")
    @ResponseBody
    public ResponseEntity logoutUserBySession(HttpSession httpSession) {

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
