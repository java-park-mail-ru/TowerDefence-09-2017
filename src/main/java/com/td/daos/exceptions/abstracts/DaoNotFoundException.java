package com.td.daos.exceptions.abstracts;

public abstract class DaoNotFoundException extends DaoException {
    private final String searchParameter;
    private final String searchParameterValue;


    public DaoNotFoundException(String entity, String searchParameter, String searchParameterValue, Throwable cause) {
        super(entity,
                entity
                        .concat(" with ")
                        .concat(searchParameter)
                        .concat(": ")
                        .concat(searchParameterValue)
                        .concat(" not found"), cause.getCause());
        this.searchParameter = searchParameter;
        this.searchParameterValue = searchParameterValue;

    }

    public String getSearchParameter() {
        return searchParameter;
    }


    public String getSearchParameterValue() {
        return searchParameterValue;
    }
}
