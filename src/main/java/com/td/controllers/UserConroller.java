package com.td.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.td.models.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/user")
public class UserConroller {

    @GetMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity getUserBySession(HttpSession httpSession) {


        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity getUserInfoById(HttpSession httpSession, @PathVariable(name = "id") Long userId) {


        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity registerUser(HttpSession httpSession, @RequestBody User user) {


        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/edit", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity editUser(HttpSession httpSession) {


        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }









}
