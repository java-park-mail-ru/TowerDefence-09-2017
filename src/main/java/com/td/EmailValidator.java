package com.td;

import java.util.regex.Pattern;

public class EmailValidator {
    public static Boolean isValid(String email) {
        final String stringPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(" +
                "([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        final Pattern commonPattern = Pattern.compile(stringPattern);
        return commonPattern.matcher(email).matches();
    }
}
