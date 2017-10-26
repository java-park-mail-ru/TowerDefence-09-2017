package com.td.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.td.dtos.TestUserDto;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testSignupSuccess() throws Exception {
        TestUserDto testUser = new TestUserDto(
                "testSignupSuccess",
                "password",
                "testSignupSuccess@mail.ru",
                null
        );
        this.mockMvc
                .perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(jsonPath("login").value("testSignupSuccess"))
                .andExpect(jsonPath("password").doesNotExist())
                .andExpect(jsonPath("email").value("testSignupSuccess@mail.ru"))
                .andExpect(jsonPath("id").exists());

    }

    @Test
    public void testLogoutAfterSignup() throws Exception {
        TestUserDto testUser = new TestUserDto(
                "testSignupSuccess",
                "password",
                "testSignupSuccess@mail.ru",
                null
        );
        Cookie session = this.mockMvc
                .perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andReturn().getResponse().getCookie("SESSION");

        this.mockMvc
                .perform(post("/auth/logout")
                        .cookie(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("Success"));
    }

    @Test
    public void testSigninAfterLogout() throws Exception {
        TestUserDto testUser = new TestUserDto(
                "testSigninAfterLogout",
                "password",
                "testSigninAfterLogout@mail.ru",
                null
        );
        Cookie session = this.mockMvc
                .perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andReturn().getResponse().getCookie("SESSION");

        this.mockMvc
                .perform(post("/auth/logout")
                        .cookie(session));

        TestUserDto signin = new TestUserDto(
                null,
                "password",
                "testSigninAfterLogout@mail.ru",
                null
        );
        this.mockMvc
                .perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signin)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("SESSION"))
                .andExpect(jsonPath("login").value("testSigninAfterLogout"))
                .andExpect(jsonPath("email").value("testSigninAfterLogout@mail.ru"))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("password").doesNotExist());
    }

    @Test
    public void testSignupWithInvalidNickname() throws Exception {
        TestUserDto invalidNickname = new TestUserDto(
                "iv",
                "password",
                "valid@mail.ru",
                null
        );
        this.mockMvc
                .perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidNickname)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value("incorrect_request_data"))
                .andExpect(jsonPath("incorrectRequestDataErrors").isArray())
                .andExpect(jsonPath("$.incorrectRequestDataErrors[0].fieldName").value("login"));
    }

    @Test
    public void testSignupWithInvalidEmail() throws Exception {
        TestUserDto invalidEmail = new TestUserDto(
                "login",
                "password",
                "invalid mail.ru",
                null
        );
        this.mockMvc
                .perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmail)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value("incorrect_request_data"))
                .andExpect(jsonPath("incorrectRequestDataErrors").isArray())
                .andExpect(jsonPath("incorrectRequestDataErrors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.incorrectRequestDataErrors[0].fieldName").value("email"));
    }

    @Test
    public void testSignupWithInvalidPassword() throws Exception {
        TestUserDto invalidPassword = new TestUserDto(
                "login",
                "ps",
                "testSignupWithInvalidNickname@mail.ru",
                null
        );
        this.mockMvc
                .perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPassword)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value("incorrect_request_data"))
                .andExpect(jsonPath("incorrectRequestDataErrors").isArray())
                .andExpect(jsonPath("incorrectRequestDataErrors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.incorrectRequestDataErrors[0].fieldName").value("password"));
    }

    @Test
    public void testSignupWithInvalidParams() throws Exception {
        TestUserDto totalInvalid = new TestUserDto(
                "iv",
                "ps",
                "invalid mail.ru",
                null
        );
        this.mockMvc
                .perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(totalInvalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value("incorrect_request_data"))
                .andExpect(jsonPath("incorrectRequestDataErrors").isArray())
                .andExpect(jsonPath("incorrectRequestDataErrors", Matchers.hasSize(3)));
    }

    @Test
    public void testLogoutWithoutSession() throws Exception {
        TestUserDto unregistered = new TestUserDto(
                "unregistered",
                "password",
                "unregistered@mail.ru",
                null
        );
        this.mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("type").value("authorization_error"));


    }

    @Test
    public void testSigninWithoutSignup() throws Exception {
        TestUserDto unregistered = new TestUserDto(
                null,
                "password",
                "unregistered@mail.ru",
                null
        );
        this.mockMvc
                .perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(unregistered)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value("incorrect_request_data"))
                .andExpect(jsonPath("$.incorrectRequestDataErrors[0].fieldName").value("email"));

    }

    @Test
    public void testSignupWithSameEmail() throws Exception {
        TestUserDto some = new TestUserDto(
                "some",
                "password",
                "some@mail.ru",
                null
        );
        TestUserDto sameEmail = new TestUserDto(
                "same",
                "password",
                "some@mail.ru",
                null
        );
        this.mockMvc
                .perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(some)))
                .andExpect(status().isOk());

        this.mockMvc
                .perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sameEmail)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value("incorrect_request_data"))
                .andExpect(jsonPath("$.incorrectRequestDataErrors[0].fieldName").value("email"));
    }

    @Test
    public void testSignupWithSameNickname() throws Exception {
        TestUserDto some = new TestUserDto(
                "some",
                "password",
                "some@mail.ru",
                null
        );
        TestUserDto sameNickname = new TestUserDto(
                "some",
                "password",
                "other@mail.ru",
                null
        );
        this.mockMvc
                .perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(some)))
                .andExpect(status().isOk());

        this.mockMvc
                .perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sameNickname)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value("incorrect_request_data"))
                .andExpect(jsonPath("$.incorrectRequestDataErrors[0].fieldName").value("login"));
    }

    @Test
    public void testSignupAlreadySignedIn() throws Exception {
        TestUserDto testUser = new TestUserDto(
                "SignupAlreadySignedIn",
                "password",
                "SASI@mail.ru",
                null
        );
        Cookie session = this.mockMvc
                .perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andReturn().getResponse().getCookie("SESSION");

        TestUserDto otherUser = new TestUserDto(
                "SASIOtherUser",
                "password",
                "SASIOtherUser@mail.ru",
                null
        );

        this.mockMvc
                .perform(post("/auth/signup")
                        .cookie(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(otherUser)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testSigninAlreadySignedIn() throws Exception {

    }


}
