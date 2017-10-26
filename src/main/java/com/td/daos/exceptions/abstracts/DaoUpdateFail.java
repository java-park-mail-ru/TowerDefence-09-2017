package com.td.daos.exceptions.abstracts;

public abstract class DaoUpdateFail extends DaoException {
    private final String field;

    public DaoUpdateFail(String entity, String field, String constrainName, Throwable cause) {
        super(entity,
                "Fail on "
                        .concat(entity)
                        .concat(" update ")
                        .concat(field)
                        .concat(" because of ")
                        .concat(constrainName), cause.getCause());

        this.field = field;
    }

    public String getField() {
        return field;
    }

}
