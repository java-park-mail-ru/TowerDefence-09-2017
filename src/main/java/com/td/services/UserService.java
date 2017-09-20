package com.td.services;

import com.td.models.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {


    private static HashMap<String, User> storedByEmail = new HashMap<>();
    private static HashMap<Long, User> storedById = new HashMap<>();

    private static Long generateId() {
        return idSerial.getAndIncrement();
    }

    private static AtomicLong idSerial = new AtomicLong(1L);

    public static final String USER_SESSION_KEY = "user";

    public static Boolean checkIfUserExists(Long id) {
        return storedById.containsKey(id);
    }

    public static Boolean checkIfUserExists(String email) {
        return storedByEmail.containsKey(email);
    }

    public static void storeUser(User newUser) {
        newUser.setId(generateId());
        updateUser(newUser);
    }

    public static void updateUser(User user) {
        storedByEmail.put(user.getEmail(), user);
        storedById.put(user.getId(), user);
    }

    public static User getUser(Long id) {
        return storedById.get(id);
    }

    public static User getUser(String email) {
        return storedByEmail.get(email);
    }

    public static User removeUser(String email) {
        return storedByEmail.remove(email);
    }

    public static User removeUser(Long id) {
        return storedById.remove(id);
    }


}
