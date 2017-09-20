package com.td.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.td.models.constraints.UserExistence;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCrypt;


@JsonAutoDetect
public class User {

    @NotBlank(message = "Login field is required")
    @JsonProperty
    private String login;

    @NotBlank(message = "Password field is required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "Email field is required")
    @Email(message = "Invalid email")
    @UserExistence(value = false, message = "Email already registered")
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