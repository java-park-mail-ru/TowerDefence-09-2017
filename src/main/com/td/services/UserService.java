package com.td.services;

import com.td.daos.inerfaces.IUserDao;
import com.td.domain.User;
import com.td.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    private final IUserDao userDao;

    public static final String USER_SESSION_KEY = "user";
    private HashMap<String, Long> storedByEmail = new HashMap<>();
    private HashMap<Long, User> storedById = new HashMap<>();
    private AtomicLong idSerial = new AtomicLong(1L);

    @Autowired
    public UserService(IUserDao userDao) {
        this.userDao = userDao;
    }

    private Long generateId() {
        return idSerial.getAndIncrement();
    }

    public Boolean checkIfUserExists(Long id) {
        return userDao.checkUser(id);
    }

    public Boolean checkIfUserExists(String email) {
        return userDao.checkUser(email);
    }

    public User storeUser(User newUser) {
        return userDao.storeUser(newUser);
    }

    public User updateUser(UserDto user) {
        return userDao.updateUserById(user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getPassword());
    }

    public User getUser(Long id) {
        return userDao.getUserById(id);
    }

    public User getUser(String email) {
        return userDao.getUserByEmail(email);
    }

    public boolean removeUser(String email) {
        /**/
        return false;
    }

    public boolean removeUser(Long id) {
        /**/
        return false;
    }


}
