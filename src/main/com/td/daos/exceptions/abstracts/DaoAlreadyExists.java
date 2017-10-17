package com.td.daos.exceptions.abstracts;

public abstract class DaoAlreadyExists extends DaoException {
    private final Long identity;

    public DaoAlreadyExists(String entity, Long identity, Throwable cause) {
        super(entity,
                "pk of "
                        .concat(entity)
                        .concat(": ")
                        .concat(identity.toString())
                        .concat("  already taken"),
                cause.getCause());

        this.identity = identity;

    }

    public Long getIdentity() {
        return identity;
    }

}
