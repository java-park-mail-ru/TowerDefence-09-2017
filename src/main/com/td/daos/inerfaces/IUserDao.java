package com.td.daos.inerfaces;

import com.td.domain.User;

import java.time.OffsetDateTime;
import java.util.List;

public interface IUserDao {
    User getUserByEmail(String email);

    boolean checkUser(String email);

    boolean checkUser(Long id);

    User getUserByNickanme(String nickname);

    User getUserById(Long id);

    List<User> getUsersByNicknames(List<String> nicknames);

    User createUser(String nickname, String email, String login);

    User storeUser(User user);

    User removeUser(User user);

    User updateUserById(Long id, String email, String login, String password);

    User updateUserEmail(User user, String email);

    User updateUserNickname(User user, String email);

    User updateUserPassword(User user, String email);

}
