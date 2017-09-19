package com.td.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.td.models.constraints.UserExistsConstraint;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@JsonAutoDetect
public class SigninForm {
    @NotBlank(message = "Email field is required")
    @Email(message = "Invalid email")
    @UserExistsConstraint
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
