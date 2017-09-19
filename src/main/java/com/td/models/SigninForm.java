package com.td.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

@JsonAutoDetect
public class SigninForm {
    @NotNull(message = "Email field is required")
    @Email(message = "Invalid email")
    private String email;

    @NotNull(message = "Password field is required")
    private String password;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
