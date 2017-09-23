package com.td.errors;

import com.fasterxml.jackson.annotation.JsonView;
import com.td.errors.views.ErrorViews;


public class AuthorizationError {

    @JsonView(ErrorViews.AuthorizationError.class)
    private String description;

    @JsonView(ErrorViews.AuthorizationError.class)
    private String path;


    public AuthorizationError(String description, String path) {
        this.description = description;
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
