package com.td.daos.exceptions;

import com.td.daos.exceptions.abstracts.DaoNotFoundException;

public class UserDaoNotFoundException extends DaoNotFoundException {

    public UserDaoNotFoundException(String searchParameter, String searchParameterValue, Throwable cause) {
        super("User", searchParameter, searchParameterValue, cause);
    }

}
