package com.td.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.td.errors.ErrorMessage;
import com.td.errors.ErrorTypes;
import com.td.errors.views.ErrorViews;
import com.td.exceptions.AuthException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = AuthException.class)
    @JsonView(ErrorViews.AuthorizationError.class)
    protected ResponseEntity<Object> handleAuthException(AuthException ex, WebRequest request) {
        ErrorMessage message = ErrorMessage
                .builder()
                .setType(ErrorTypes.AUTHORIZATION_ERROR)
                .setAuthorizationErrors(ex.getError())
                .build();
        return handleExceptionInternal(ex, message,
                new HttpHeaders(), ex.getStatus(), request);
    }
}

