package com.td.services;

import com.td.models.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {


    public static final String USER_SESSION_KEY = "user";
    private static HashMap<String, Long> storedByEmail = new HashMap<>();
    private static HashMap<Long, User> storedById = new HashMap<>();
    private static AtomicLong idSerial = new AtomicLong(1L);

    private static Long generateId() {
        return idSerial.getAndIncrement();
    }

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

    private static void updateUser(User user) {
        storedByEmail.put(user.getEmail(), user.getId());
        storedById.put(user.getId(), user);
    }

    public static User getUser(Long id) {
        return storedById.get(id);
    }

    public static User getUser(String email) {
        Long id = storedByEmail.get(email);
        if (id != null) {
            return storedById.get(id);
        }
        return null;
    }

    public static boolean removeUser(String email) {
        Long id = storedByEmail.get(email);
        if (id != null) {
            storedById.remove(id);
            storedByEmail.remove(email);
            return true;
        }
        return false;
    }

    public static boolean removeUser(Long id) {
        User user = storedById.get(id);
        if (user != null) {
            storedByEmail.remove(user.getEmail());
            storedById.remove(id);
            return true;
        }
        return false;
    }


}
