package com.td.daos.exceptions.abstracts;

public abstract class DaoException extends RuntimeException {
    private final String entity;

    public DaoException(String entity, String message, Throwable cause) {
        super(message, cause);
        this.entity = entity;
    }

    public String getEntity() {
        return entity;
    }
}
