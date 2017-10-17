package com.td.daos;

import com.td.domain.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

    static final private String EMAIL_SUFFIX = "@mail.ru";
    @Rule
    public final ExpectedException exp = ExpectedException.none();

    @Before
    public void init() {
        this.uuids = Stream
                .generate(() -> UUID.randomUUID().toString().substring(0, 25))
                .limit(25)
                .collect(Collectors.toList());
        uuids.forEach(uuid -> {
            User user = dao.createUser(uuid, uuid + EMAIL_SUFFIX, uuid);
            assertEquals(user.getEmail(), uuid + EMAIL_SUFFIX);
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
            User byEmailUser = dao.getUserByEmail(uuid + EMAIL_SUFFIX);
            User byNicknameUser = dao.getUserByNickanme(uuid);
            assertEquals(byEmailUser, byNicknameUser);
            assertEquals(byEmailUser.getNickname(), uuid);
            assertEquals(byEmailUser.getEmail(), uuid + EMAIL_SUFFIX);
        });
    }

    @Test
    public void testUserFullUpdate() {
        String str = "update";
        String email = str + EMAIL_SUFFIX;
        uuids.forEach(uuid -> {
            User user = dao.getUserByNickanme(uuid);
            dao.updateUserById(user.getId(), email, str, str);

            assertEquals(user.getEmail(), email);
            assertEquals(user.getNickname(), str);
            assertTrue(user.checkPassword(str));

            dao.updateUserById(user.getId(), uuid + EMAIL_SUFFIX, uuid, uuid);
            assertEquals(user.getEmail(), uuid + EMAIL_SUFFIX);
            assertEquals(user.getNickname(), uuid);
            assertTrue(user.checkPassword(uuid));
        });
    }

    @Test
    public void testUserEmailUpdate() {
        String str = "update";
        String email = str + EMAIL_SUFFIX;
        uuids.forEach(uuid -> {
            User user = dao.getUserByNickanme(uuid);
            dao.updateUserEmail(user, email);

            assertEquals(user.getEmail(), email);
            assertEquals(user.getNickname(), uuid);
            assertTrue(user.checkPassword(uuid));

            dao.updateUserById(user.getId(), uuid + EMAIL_SUFFIX, null, null);

            assertEquals(user.getEmail(), uuid + EMAIL_SUFFIX);
            assertEquals(user.getNickname(), uuid);
            assertTrue(user.checkPassword(uuid));
        });
    }

    @Test
    public void testUserNicknameUpdate() {
        String str = "update";
        uuids.forEach(uuid -> {
            User user = dao.getUserByNickanme(uuid);
            dao.updateUserNickname(user, str);

            assertEquals(user.getEmail(), uuid + EMAIL_SUFFIX);
            assertEquals(user.getNickname(), str);
            assertTrue(user.checkPassword(uuid));

            dao.updateUserById(user.getId(), null, uuid, null);

            assertEquals(user.getEmail(), uuid + EMAIL_SUFFIX);
            assertEquals(user.getNickname(), uuid);
            assertTrue(user.checkPassword(uuid));
        });
    }

    @Test
    public void testUserPasswordUpdate() {
        String str = "update";
        uuids.forEach(uuid -> {
            User user = dao.getUserByNickanme(uuid);
            dao.updateUserPassword(user, str);

            assertEquals(user.getEmail(), uuid + EMAIL_SUFFIX);
            assertEquals(user.getNickname(), uuid);
            assertTrue(user.checkPassword(str));

            dao.updateUserById(user.getId(), null, null, uuid);

            assertEquals(user.getEmail(), uuid + EMAIL_SUFFIX);
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

    @Test
    public void testUserRemoveById() {
        uuids.forEach(uuid -> {
            User user = dao.getUserByNickanme(uuid);
            dao.removeUserById(user.getId());
            assertFalse(dao.checkUser(user.getId()));
            User recreated = dao.createUser(user.getNickname(), user.getEmail(), user.getPassword());
            assertEquals(recreated.getNickname(), user.getNickname());
            assertEquals(recreated.getEmail(), user.getEmail());
        });
    }

    @Test
    public void testUserRemoveByEmail() {
        uuids.forEach(uuid -> {
            User user = dao.getUserByNickanme(uuid);
            dao.removeUserByEmail(user.getEmail());
            assertFalse(dao.checkUser(user.getId()));
            User recreated = dao.createUser(user.getNickname(), user.getEmail(), user.getPassword());
            assertEquals(recreated.getNickname(), user.getNickname());
            assertEquals(recreated.getEmail(), user.getEmail());
        });
    }

    @Test
    public void testUserRemoveByNickname() {
        uuids.forEach(uuid -> {
            User user = dao.getUserByNickanme(uuid);
            dao.removeUserByNickname(user.getNickname());
            assertFalse(dao.checkUser(user.getId()));
            User recreated = dao.createUser(user.getNickname(), user.getEmail(), user.getPassword());
            assertEquals(recreated.getNickname(), user.getNickname());
            assertEquals(recreated.getEmail(), user.getEmail());
        });
    }

    @Test
    public void testUserRemoveByParams() {
        uuids.forEach(uuid -> {
            int expZero = dao.removeUserByParams(null, null, null);
            assertEquals(0, expZero);

            User user = dao.getUserByNickanme(uuid);
            int expOne = dao.removeUserByParams(user.getId(), user.getEmail(), user.getNickname());
            assertFalse(dao.checkUser(user.getId()));
            assertEquals(1, expOne);

            User recreated = dao.createUser(user.getNickname(), user.getEmail(), user.getPassword());
            dao.removeUser(recreated);
            assertFalse(dao.checkUser(recreated.getId()));
            dao.createUser(user.getNickname(), user.getEmail(), user.getPassword());
        });
        User initial = dao.createUser("new", "new" + EMAIL_SUFFIX, "new");
        uuids.stream()
                .reduce(initial.getNickname(),
                        (prev, current) -> {
                            int expTwo = dao.removeUserByParams(null, prev + EMAIL_SUFFIX, current);
                            assertEquals(2, expTwo);
                            dao.createUser(prev, prev + EMAIL_SUFFIX, prev);
                            dao.createUser(current, current + EMAIL_SUFFIX, current);
                            return current;
                        });
    }
}
