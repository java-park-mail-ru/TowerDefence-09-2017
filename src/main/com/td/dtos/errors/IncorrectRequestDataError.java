package com.td.dtos.errors;

import com.fasterxml.jackson.annotation.JsonView;
import com.td.dtos.errors.views.ErrorViews;

public class IncorrectRequestDataError {

    @JsonView(ErrorViews.IncorrectRequestData.class)
    private String fieldName;

    @JsonView(ErrorViews.IncorrectRequestData.class)
    private String description;


    public IncorrectRequestDataError(String fieldName, String description) {
        this.fieldName = fieldName;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
