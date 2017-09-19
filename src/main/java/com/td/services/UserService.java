package com.td.services;

import com.td.models.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserService {

    private static HashMap<String, User> storedByEmail = new HashMap<>();
    private static HashMap<Long, User> storedById = new HashMap<>();

    private static Long idSerial = 1L;

    public static Boolean checkIfUserExists(Long id) {
        return storedById.containsKey(id);
    }

    public static Boolean checkIfUserExists(String email) {
        return storedByEmail.containsKey(email);
    }

    public static void addUser(User newUser) {
        newUser.setId(generateId());
        storedByEmail.put(newUser.getEmail(), newUser);
        storedById.put(newUser.getId(), newUser);
    }

    public static User getUser(Long id) {
        return storedById.get(id);
    }

    public static User getUser(String email) {
        return storedByEmail.get(email);
    }

    private static Long generateId(){
        return idSerial++;
    }


}
