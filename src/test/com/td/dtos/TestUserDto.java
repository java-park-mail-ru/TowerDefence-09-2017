package com.td.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class TestUserDto {
    private String login;

    private String password;

    private String email;

    private Long id;

    @JsonCreator
    public TestUserDto(@JsonProperty("login") String login,
                       @JsonProperty("password") String password,
                       @JsonProperty("email") String email,
                       @JsonProperty("id") Long id) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.id = id;
    }

    public TestUserDto() {
        this.login = "";
        this.password = "";
        this.email = "";
        this.id = 0L;
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
        this.password = password;
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
