package com.td.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.td.dtos.TestUserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testSignupAndGetCurrentUser() throws Exception {
        TestUserDto user = new TestUserDto(
                "login",
                "password",
                "email@mail.ru",
                null,
                "Adventurer"
        );
        Cookie session = this.mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andReturn()
                .getResponse()
                .getCookie("SESSION");


        this.mockMvc
                .perform(get("/api/user")
                        .cookie(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("login").value("login"))
                .andExpect(jsonPath("email").value("email@mail.ru"))
                .andExpect(jsonPath("password").doesNotExist())
                .andExpect(jsonPath("id").isNumber());
    }

    @Test
    public void testEditAfterSignup() throws Exception {
        TestUserDto user = new TestUserDto(
                "login",
                "password",
                "email@mail.ru",
                null,
                "Adventurer"
        );
        MockHttpServletResponse response = this.mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andReturn()
                .getResponse();

        Cookie session = response.getCookie("SESSION");
        TestUserDto returned = objectMapper.readerFor(TestUserDto.class).readValue(response.getContentAsString());
        TestUserDto update = new TestUserDto(
                "newLogin",
                null,
                null,
                returned.getId(),
                "Adventurer"
        );
        this.mockMvc
                .perform(post("/api/user/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update))
                        .cookie(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("login").value("newLogin"))
                .andExpect(jsonPath("email").value("email@mail.ru"))
                .andExpect(jsonPath("password").doesNotExist())
                .andExpect(jsonPath("id").isNumber());

        update.setLogin(null);
        update.setEmail("newEmail@mail.ru");

        this.mockMvc
                .perform(post("/api/user/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update))
                        .cookie(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("login").value("newLogin"))
                .andExpect(jsonPath("email").value("newEmail@mail.ru"))
                .andExpect(jsonPath("password").doesNotExist())
                .andExpect(jsonPath("id").isNumber());
    }

    @Test
    public void testGetUserDataWithoutSession() throws Exception {

        this.mockMvc
                .perform(get("/api/user"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("type").value("authorization_error"));
    }

    @Test
    public void testEditUserDataWithoutSession() throws Exception {
        TestUserDto user = new TestUserDto(
                "login",
                "password",
                "email@mail.ru",
                null,
                "Adventurer"
        );
        MockHttpServletResponse response = this.mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andReturn()
                .getResponse();

        TestUserDto returned = objectMapper.readerFor(TestUserDto.class).readValue(response.getContentAsString());
        TestUserDto update = new TestUserDto(
                "newLogin",
                null,
                null,
                returned.getId(),
                "Adventuer"
        );
        this.mockMvc
                .perform(post("/api/user/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("type").value("authorization_error"));
    }

    @Test
    public void testEditOthersUserData() throws Exception {
        TestUserDto user = new TestUserDto(
                "login",
                "password",
                "email@mail.ru",
                null,
                "Adventurer"
        );
        MockHttpServletResponse response = this.mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andReturn()
                .getResponse();

        Cookie session = response.getCookie("SESSION");
        TestUserDto returned = objectMapper.readerFor(TestUserDto.class).readValue(response.getContentAsString());
        TestUserDto update = new TestUserDto(
                "newLogin",
                null,
                null,
                returned.getId() + 1,
                "Adventurer"
        );
        this.mockMvc
                .perform(post("/api/user/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update))
                        .cookie(session))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("type").value("authorization_error"));
    }
}
