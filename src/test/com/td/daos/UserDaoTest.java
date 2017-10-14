package com.td.daos;

import com.td.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class UserDaoTest {
    @Autowired
    private UserDao dao;

    private List<String> uuids;

    @Before
    public void init() {
        this.uuids = Stream
                .generate(() -> UUID.randomUUID().toString().substring(0, 25))
                .limit(25)
                .collect(Collectors.toList());
        uuids.forEach(uuid -> {
            User user = dao.createUser(uuid, uuid + "@mail.ru", uuid);
            assertEquals(user.getEmail(), uuid + "@mail.ru");
            assertEquals(user.getNickname(), uuid);
            assertTrue(user.checkPassword(uuid));
            assertTrue(dao.checkUser(user.getId()));
            assertTrue(dao.checkUser(user.getEmail()));
            assertNotNull(user.getId());
        });
    }

    @Test
    public void testUserGet() {
        uuids.forEach(uuid -> {
            User byEmailUser = dao.getUserByEmail(uuid + "@mail.ru");
            User byNicknameUser = dao.getUserByNickanme(uuid);
            assertEquals(byEmailUser, byNicknameUser);
            assertEquals(byEmailUser.getNickname(), uuid);
            assertEquals(byEmailUser.getEmail(), uuid + "@mail.ru");
        });
    }

    @Test
    public void testUserFullUpdate() {
        uuids.forEach(uuid -> {
            User user = dao.getUserByNickanme(uuid);
            dao.updateUserById(user.getId(), "update@mail.ru", "update", "update");

            assertEquals(user.getEmail(), "update@mail.ru");
            assertEquals(user.getNickname(), "update");
            assertTrue(user.checkPassword("update"));

            dao.updateUserById(user.getId(), uuid + "@mail.ru", uuid, uuid);
            assertEquals(user.getEmail(), uuid + "@mail.ru");
            assertEquals(user.getNickname(), uuid);
            assertTrue(user.checkPassword(uuid));
        });
    }

    @Test
    public void testUserEmailUpdate() {
        uuids.forEach(uuid -> {
            User user = dao.getUserByNickanme(uuid);
            dao.updateUserEmail(user, "update@mail.ru");

            assertEquals(user.getEmail(), "update@mail.ru");
            assertEquals(user.getNickname(), uuid);
            assertTrue(user.checkPassword(uuid));

            dao.updateUserById(user.getId(), uuid + "@mail.ru", null, null);

            assertEquals(user.getEmail(), uuid + "@mail.ru");
            assertEquals(user.getNickname(), uuid);
            assertTrue(user.checkPassword(uuid));
        });
    }

    @Test
    public void testUserNicknameUpdate() {
        uuids.forEach(uuid -> {
            User user = dao.getUserByNickanme(uuid);
            dao.updateUserNickname(user, "update");

            assertEquals(user.getEmail(), uuid + "@mail.ru");
            assertEquals(user.getNickname(), "update");
            assertTrue(user.checkPassword(uuid));

            dao.updateUserById(user.getId(), null, uuid, null);

            assertEquals(user.getEmail(), uuid + "@mail.ru");
            assertEquals(user.getNickname(), uuid);
            assertTrue(user.checkPassword(uuid));
        });
    }

    @Test
    public void testUserPasswordUpdate() {
        uuids.forEach(uuid -> {
            User user = dao.getUserByNickanme(uuid);
            dao.updateUserPassword(user, "update");

            assertEquals(user.getEmail(), uuid + "@mail.ru");
            assertEquals(user.getNickname(), uuid);
            assertTrue(user.checkPassword("update"));

            dao.updateUserById(user.getId(), null, null, uuid);

            assertEquals(user.getEmail(), uuid + "@mail.ru");
            assertEquals(user.getNickname(), uuid);
            assertTrue(user.checkPassword(uuid));
        });
    }

    @Test
    public void testUserGetUsersByNicknames() {
        Map<String, User> trusted = uuids.stream().collect(Collectors.toMap(
                uuid -> uuid,
                uuid -> dao.getUserByNickanme(uuid)
        ));
        dao.getUsersByNicknames(uuids)
                .forEach(user -> assertEquals(trusted.get(user.getNickname()), user));
    }
}
