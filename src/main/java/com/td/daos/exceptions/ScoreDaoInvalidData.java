package com.td.daos.exceptions;

import com.td.daos.exceptions.abstracts.DaoInvalidData;

public class ScoreDaoInvalidData extends DaoInvalidData {

    public ScoreDaoInvalidData(String constraintName, Throwable cause) {
        super("Score", constraintName, cause);
    }

}
