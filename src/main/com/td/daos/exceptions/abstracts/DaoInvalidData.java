package com.td.daos.exceptions.abstracts;

public abstract class DaoInvalidData extends DaoException {
    private final String constraintName;

    public DaoInvalidData(String entity, String constraintName, Throwable cause) {
        super(entity,
                "Invalid "
                        .concat(entity)
                        .concat(" data comes to database"), cause.getCause());
        this.constraintName = constraintName;

    }

    public String getConstraintName() {
        return constraintName;
    }

}
