package com.td.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.td.dtos.constraints.UserExistence;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@JsonAutoDetect
public class SigninFormDto {
    @NotBlank(message = "Email field is required")
    @Email(message = "Invalid email")
    @UserExistence(value = true, message = "Email doesn't correspond any user")
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
