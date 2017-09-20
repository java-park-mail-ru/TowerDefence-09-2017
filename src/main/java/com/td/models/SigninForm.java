package com.td.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.td.models.constraints.UserExistence;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@JsonAutoDetect
public class SigninForm {
    @NotBlank(message = "Email field is required")
    @Email(message = "Invalid email")
    @UserExistence(value = true, message = "Email doesn't correspond any user")
    private String email;

    @NotBlank(message = "Password field is required")
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
