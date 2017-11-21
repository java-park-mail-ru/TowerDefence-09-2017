package com.td.daos;

import com.td.daos.exceptions.UserDaoInvalidData;
import com.td.daos.exceptions.UserDaoUpdateFail;
import com.td.domain.GameProfile;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
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
                .limit(5)
                .collect(Collectors.toList());
        uuids.forEach(uuid -> {
            User user = dao.createUser(uuid, uuid + EMAIL_SUFFIX, uuid);
            assertEquals(user.getEmail(), uuid + EMAIL_SUFFIX);
            assertEquals(user.getNickname(), uuid);
            assertTrue(user.checkPassword(uuid));
            assertTrue(dao.checkUserById(user.getId()));
            assertTrue(dao.checkUserByEmail(user.getEmail()));
            assertEquals(user.getProfile().getGameClass(), "Adventurer");
            assertNotNull(user.getId());
        });
    }

    @Test
    public void testUserGet() {
        uuids.forEach(uuid -> {
            User byEmailUser = dao.getUserByEmail(uuid + EMAIL_SUFFIX);
            User byNicknameUser = dao.getUserByNickname(uuid);
            assertEquals(byEmailUser, byNicknameUser);
            assertEquals(byEmailUser.getNickname(), uuid);
            assertEquals(byEmailUser.getEmail(), uuid + EMAIL_SUFFIX);
            assertNotNull(byEmailUser.getProfile());
        });
    }

    @Test
    public void testUserFullUpdate() {
        String str = "update";
        String email = str + EMAIL_SUFFIX;
        uuids.forEach(uuid -> {
            User user = dao.getUserByNickname(uuid);
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
            User user = dao.getUserByNickname(uuid);
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
            User user = dao.getUserByNickname(uuid);
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
            User user = dao.getUserByNickname(uuid);
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
                uuid -> dao.getUserByNickname(uuid)
        ));
        dao.getUsersByNicknames(uuids)
                .forEach(user -> assertEquals(trusted.get(user.getNickname()), user));
    }

    @Test
    public void testUserRemoveById() {
        uuids.forEach(uuid -> {
            User user = dao.getUserByNickname(uuid);
            dao.removeUserById(user.getId());
            assertFalse(dao.checkUserById(user.getId()));

        });
    }

    @Test
    public void testUserRemoveByEmail() {
        uuids.forEach(uuid -> {
            User user = dao.getUserByNickname(uuid);

            dao.removeUserByEmail(user.getEmail());
            assertFalse(dao.checkUserById(user.getId()));

        });
    }

    @Test
    public void testUserRemoveByNickname() {
        uuids.forEach(uuid -> {
            User user = dao.getUserByNickname(uuid);
            dao.removeUserByNickname(user.getNickname());
            assertFalse(dao.checkUserById(user.getId()));

        });
    }

    @Test
    public void testUserRemoveByParams() {
        int expZero = dao.removeUserByParams(null, null, null);
        assertEquals(0, expZero);

        User user = dao.getUserByNickname(uuids.get(0));
        int expOne = dao.removeUserByParams(user.getId(), user.getEmail(), user.getNickname());
        assertFalse(dao.checkUserById(user.getId()));
        assertEquals(1, expOne);

        int expTwo = dao.removeUserByParams(null, uuids.get(1) + EMAIL_SUFFIX, uuids.get(2));
        assertFalse(dao.checkUserByEmail(uuids.get(1) + EMAIL_SUFFIX));
        assertFalse(dao.checkUserByNickname(uuids.get(2)));
        assertEquals(2, expTwo);
    }

    @Test
    public void testUserRemove() {
        User user = dao.getUserByNickname(uuids.get(0));
        dao.removeUser(user);
        assertFalse(dao.checkUserByNickname(uuids.get(0)));
    }

    @Test
    public void testDuplicateUserNickname() {
        String str = uuids.get(0);
        exp.expect(UserDaoInvalidData.class);
        dao.createUser(str, str + str + EMAIL_SUFFIX, str);

    }

    @Test
    public void testDuplicateUserEmail() {
        String str = uuids.get(0);
        exp.expect(UserDaoInvalidData.class);
        dao.createUser("new", str + EMAIL_SUFFIX, str);
    }

    @Test
    public void testDuplicateUserNicknameUpdate() {
        String str = uuids.get(0);
        exp.expect(UserDaoUpdateFail.class);
        dao.updateUserNickname(dao.getUserByNickname(str), uuids.get(1));
    }

    @Test
    public void testDuplicateUserEmailUpdate() {
        String str = uuids.get(0);
        exp.expect(UserDaoUpdateFail.class);
        dao.updateUserEmail(dao.getUserByNickname(str), uuids.get(1) + EMAIL_SUFFIX);
    }


}
