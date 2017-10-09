package com.td.service;


import com.td.models.User;
import com.td.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    private final List<User> testData = new ArrayList<>();

    private static final int TEST_DATA_SIZE = 10;

    @Before
    public void init() {
        for (int i = 0; i < TEST_DATA_SIZE; ++i) {
            User user = new User();
            String uuid = UUID.randomUUID().toString();
            user.setEmail(uuid.concat("@mail.ru"));
            user.setLogin(uuid);
            user.setPassword(uuid);
            testData.add(user);
        }
        for (User user : testData) {
            userService.storeUser(user);
        }
    }

    @Test
    public void testUserStore() {
        for (User user : testData) {
            assertTrue(userService.checkIfUserExists(user.getEmail()));
        }
    }

    @Test
    public void testUserGet() {
        for (User user : testData) {
            User storedByEmail = userService.getUser(user.getEmail());
            User storedById = userService.getUser(user.getId());
            assertEquals(storedByEmail, storedById);
        }
    }

    @Test
    public void testUpdateUser() {
        for (User user : testData) {
            String uuid = UUID.randomUUID().toString();
            user.setLogin(uuid);
            user.setPassword(uuid);
            user.setEmail(uuid.concat("@mail.ru"));
            userService.updateUser(user);
            User updated = userService.getUser(user.getEmail());
            assertEquals(updated.getLogin(), user.getLogin());
            assertEquals(updated.getEmail(), user.getEmail());
            assertTrue(updated.checkPassword(uuid));
        }
    }


    @Test
    public void testRemoveUser() {
        for (User user : testData) {
            userService.removeUser(user.getEmail());
            assertFalse(userService.checkIfUserExists(user.getEmail()));
            assertFalse(userService.checkIfUserExists(user.getLogin()));
        }
    }

}
