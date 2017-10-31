package com.td.daos.exceptions;

import com.td.daos.exceptions.abstracts.DaoInvalidData;

public class UserDaoInvalidData extends DaoInvalidData {

    public UserDaoInvalidData(String constraintName, Throwable cause) {
        super("User", constraintName, cause.getCause());
    }
}
