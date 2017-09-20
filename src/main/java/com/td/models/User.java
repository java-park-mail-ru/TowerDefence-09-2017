package com.td.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.crypto.bcrypt.BCrypt;


@JsonAutoDetect
public class User {
    @JsonProperty
    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty
    private String email;

    @JsonProperty
    private Long id;

    @JsonCreator
    public User(@JsonProperty("login") String login,
                @JsonProperty("password") String password,
                @JsonProperty("email") String email,
                @JsonProperty(value = "id") Long id) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.id = id;
    }

    public User(@JsonProperty("login") String login,
                @JsonProperty("password") String password,
                @JsonProperty("email") String email) {
        this.login = login;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.email = email;
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
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
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