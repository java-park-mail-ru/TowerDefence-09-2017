package com.td.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.td.dtos.constraints.PasswordValid;
import com.td.dtos.constraints.UserExistence;
import com.td.dtos.groups.NewUser;
import com.td.dtos.groups.UpdateUser;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@JsonAutoDetect
public class UserDto {

    @NotBlank(message = "Login field is required", groups = NewUser.class)
    @JsonProperty
    private String login;


    @NotNull(message = "Password field is required", groups = NewUser.class)
    @PasswordValid
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "Email field is required", groups = NewUser.class)
    @Email(message = "Invalid email")
    @UserExistence(value = false, message = "Email already registered")
    @JsonProperty
    private String email;

    @JsonProperty
    @NotNull(message = "Id field is required", groups = UpdateUser.class)
    private Long id;

    @JsonCreator
    public UserDto(@JsonProperty("login") String login,
                   @JsonProperty("password") String password,
                   @JsonProperty("email") String email,
                   @JsonProperty("id") Long id) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.id = id;
    }

    public UserDto() {
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
