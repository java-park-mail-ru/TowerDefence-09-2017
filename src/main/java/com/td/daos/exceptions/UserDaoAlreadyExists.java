package com.td.daos.exceptions;

import com.td.daos.exceptions.abstracts.DaoAlreadyExists;

public class UserDaoAlreadyExists extends DaoAlreadyExists {

    public UserDaoAlreadyExists(Long identity, Throwable cause) {
        super("User", identity, cause);
    }
}
