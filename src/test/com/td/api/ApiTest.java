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
import org.springframework.http.*;
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
        hardRegister(temporaryUser);
    }

    @Test
    public void signinSuccesful() {
        login(registeredUser);
    }

    @Test
    public void logoutSuccessful() {
        List<String> cookies = login(registeredUser);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<Void> requestEntity = new HttpEntity<>(requestHeaders);

        ResponseEntity<UserDto> logoutResponse =
                restTemplate.exchange("/auth/logout", HttpMethod.GET, requestEntity, UserDto.class);

        assertEquals(HttpStatus.OK, logoutResponse.getStatusCode());

        UserDto user = logoutResponse.getBody();
        assertNotNull(user);

        assertEquals(registeredUser.getEmail(), user.getEmail());
        assertEquals(registeredUser.getLogin(), user.getLogin());
        assertEquals(registeredUser.getId(), user.getId());

    }

    @Test
    public void getUserInfo() {
        List<String> cookies = login(registeredUser);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<Void> requestEntity = new HttpEntity<>(requestHeaders);

        ResponseEntity<UserDto> infoResponse =
                restTemplate.exchange("/user", HttpMethod.GET, requestEntity, UserDto.class);

        assertEquals(HttpStatus.OK, infoResponse.getStatusCode());

        UserDto user = infoResponse.getBody();
        assertNotNull(user);

        assertEquals(registeredUser.getEmail(), user.getEmail());
        assertEquals(registeredUser.getLogin(), user.getLogin());
        assertEquals(registeredUser.getId(), user.getId());
    }

    @Test
    public void editUserInfo() {
        TestUserDto userToEdit = new TestUserDto(
                "abcdefgh",
                "abcdefgh",
                "abcdefgh@mail.ru",
                0L
        );
        //register user with this fields
        List<String> cookies = hardRegister(userToEdit);

        TestUserDto editedUser = userToEdit;

        //change fields which is available to edit
        userToEdit.setLogin("abcdefgh1");
        userToEdit.setPassword("abcdefgh1");
        userToEdit.setEmail("abcdefgh1@mail.ru");

        //creating request body and headers, also putting cookies into request headers
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<TestUserDto> requestEntity = new HttpEntity<>(editedUser, requestHeaders);

        //receiving response from controller
        ResponseEntity<UserDto> editResponse =
                restTemplate.postForEntity("/user/edit", requestEntity, UserDto.class);

        assertEquals(HttpStatus.OK, editResponse.getStatusCode());

        //checking cookies, and resetting it
        cookies = editResponse.getHeaders().get("Set-Cookie");
        assertNotNull(cookies);
        assertFalse(cookies.isEmpty());

        UserDto user = editResponse.getBody();
        assertNotNull(user);
        
        assertEquals(editedUser.getEmail(), user.getEmail());
        assertEquals(editedUser.getLogin(), user.getLogin());
        assertEquals(editedUser.getId(), user.getId());
    }

    private List<String> login(TestUserDto userToLogin) {
        HttpEntity<TestUserDto> requestEntity = new HttpEntity<>(userToLogin);
        ResponseEntity<UserDto> loginResponse =
                restTemplate.postForEntity("/auth/signin", requestEntity, UserDto.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        List<String> cookies = loginResponse.getHeaders().get("Set-Cookie");
        assertNotNull(cookies);
        assertFalse(cookies.isEmpty());

        UserDto user = loginResponse.getBody();
        assertNotNull(user);

        assertEquals(userToLogin.getEmail(), user.getEmail());
        assertEquals(userToLogin.getLogin(), user.getLogin());
        assertEquals(userToLogin.getId(), user.getId());
        return cookies;
    }

    private List<String> hardRegister(TestUserDto userToRegister) {
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

        return cookies;
    }
}
