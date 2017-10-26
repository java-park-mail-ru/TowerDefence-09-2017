package com.td.daos.exceptions;

import com.td.daos.exceptions.abstracts.DaoUpdateFail;

public class UserDaoUpdateFail extends DaoUpdateFail {
    private String field;

    public UserDaoUpdateFail(String field, String constrainName, Throwable cause) {
        super("User", field, constrainName, cause.getCause());
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
