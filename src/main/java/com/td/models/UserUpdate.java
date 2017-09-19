package com.td.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.td.models.constraints.UserExistance;
import org.hibernate.validator.constraints.Email;

@JsonAutoDetect
public class UserUpdate {
    @Email(message = "Invalid email")
    @UserExistance(value = false, message = "Email is alredy registered")
    private String email;

    private String password;

    private String login;

    private Long id;


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public Long getId() {
        return id;
    }
}
