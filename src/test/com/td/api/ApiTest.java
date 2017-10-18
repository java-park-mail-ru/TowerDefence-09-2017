package com.td.api;


import com.td.dtos.TestUserDto;
import com.td.dtos.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is a functional tester for all API methods
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Transactional
public class ApiTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private TestUserDto registeredUser = new TestUserDto(
            "abcdefg",
            "abcdefg",
            "abcdefg@mail.ru",
            0L
    );

    private TestUserDto temporaryUser = new TestUserDto(
            "1234567",
            "1234567",
            "1234567@mail.ru",
            0L
    );


    @Before
    public void initialize() {
        hardRegister(registeredUser);
    }

    @Test
    public void signupSuccessful() {
        HttpEntity<TestUserDto> requestEntity = new HttpEntity<>(temporaryUser);
        ResponseEntity<UserDto> registerResponse =
                restTemplate.postForEntity("/auth/signup", requestEntity, UserDto.class);

        assertEquals(HttpStatus.CREATED, registerResponse.getStatusCode());

        List<String> cookies = registerResponse.getHeaders().get("Set-Cookie");
        assertNotNull(cookies);
        assertFalse(cookies.isEmpty());

        UserDto user = registerResponse.getBody();
        assertNotNull(user);

        assertNotNull(user.getId());
        temporaryUser.setId(user.getId());

        assertEquals(temporaryUser.getEmail(), user.getEmail());
        assertEquals(temporaryUser.getLogin(), user.getLogin());
    }

    //@Test
    public void signinSuccesful() {
        login(registeredUser);
    }

    private List<String> login(TestUserDto generatedUser) {
        hardRegister(generatedUser);
        HttpEntity<TestUserDto> requestEntity = new HttpEntity<>(generatedUser);
        ResponseEntity<UserDto> loginResponse =
                restTemplate.postForEntity("/auth/signin", requestEntity, UserDto.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        List<String> cookies = loginResponse.getHeaders().get("Set-Cookie");
        assertNotNull(cookies);
        assertFalse(cookies.isEmpty());

        UserDto user = loginResponse.getBody();
        assertNotNull(user);

        assertEquals(generatedUser.getEmail(), user.getEmail());
        assertEquals(generatedUser.getLogin(), user.getLogin());
        assertEquals(generatedUser.getId(), user.getId());
        return cookies;
    }

    private void hardRegister(TestUserDto userToRegister) {
        HttpEntity<TestUserDto> requestEntity = new HttpEntity<>(userToRegister);
        ResponseEntity<UserDto> registerResponse =
                restTemplate.postForEntity("/auth/signup", requestEntity, UserDto.class);

        assertEquals(HttpStatus.CREATED, registerResponse.getStatusCode());

        List<String> cookies = registerResponse.getHeaders().get("Set-Cookie");
        assertNotNull(cookies);
        assertFalse(cookies.isEmpty());


        UserDto user = registerResponse.getBody();
        assertNotNull(user);

        assertNotNull(user.getId());
        userToRegister.setId(user.getId());

        assertEquals(userToRegister.getEmail(), user.getEmail());
        assertEquals(userToRegister.getLogin(), user.getLogin());
    }
}
