package com.td.dtos.errors;

import com.fasterxml.jackson.annotation.JsonView;
import com.td.dtos.errors.views.ErrorViews;

public class TryAgainError {

    @JsonView(ErrorViews.TryAgainError.class)
    private String message;

    public TryAgainError(String message) {
        this.message = message;
    }

}
