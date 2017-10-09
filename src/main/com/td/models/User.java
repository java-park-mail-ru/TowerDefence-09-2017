package com.td.models;

import org.springframework.security.crypto.bcrypt.BCrypt;


public class User {

    private String login;

    private String password;

    private String email;

    private Long id;

    public User() {
        this.login = "";
        this.password = "";
        this.email = "";
        this.id = 0L;
    }

    private String hashPassword(String somePassword) {
        return BCrypt.hashpw(somePassword, BCrypt.gensalt());
    }

    public boolean checkPassword(String outerPassword) {
        return BCrypt.checkpw(outerPassword, this.password);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}