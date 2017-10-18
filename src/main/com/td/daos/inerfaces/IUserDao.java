package com.td.daos.inerfaces;

import com.td.domain.User;

import java.util.List;

public interface IUserDao {
    User getUserByEmail(String email);

    boolean checkUserByEmail(String email);

    boolean checkUserByNickname(String nickname);

    boolean checkUserById(Long id);

    User getUserByNickname(String nickname);

    User getUserById(Long id);

    List<User> getUsersByNicknames(List<String> nicknames);

    User createUser(String nickname, String email, String password);

    User storeUser(User user);

    void removeUser(User user);

    void removeUserById(Long id);

    void removeUserByEmail(String email);

    void removeUserByNickname(String nickname);

    int removeUserByParams(Long id, String email, String nickname);

    User updateUserById(Long id, String email, String login, String password);

    User updateUserEmail(User user, String email);

    User updateUserNickname(User user, String email);

    User updateUserPassword(User user, String email);
}
