package com.td.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.td.daos.exceptions.abstracts.DaoException;
import com.td.daos.exceptions.abstracts.DaoInvalidData;
import com.td.dtos.errors.ErrorMessage;
import com.td.dtos.errors.ErrorTypes;
import com.td.dtos.errors.TryAgainError;
import com.td.dtos.errors.views.ErrorViews;
import com.td.exceptions.AuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalResponseEntityExceptionHandler.class);

    protected ResponseEntity<Object> handleAuthException(AuthException ex, WebRequest request) {
        log.error("Autthentication error: ", ex);
        ErrorMessage message = ErrorMessage
                .builder()
                .setType(ErrorTypes.AUTHORIZATION_ERROR)
                .setAuthorizationErrors(ex.getError())
                .build();
        return handleExceptionInternal(ex, message,
                new HttpHeaders(), ex.getStatus(), request);
    }

    @ExceptionHandler(value = DaoException.class)
    @JsonView(ErrorViews.TryAgainError.class)
    protected ResponseEntity<Object> handleDaoInvalidDataException(DaoInvalidData ex, WebRequest request) {
        log.error("Dao internal error: ", ex);
        ErrorMessage message = ErrorMessage
                .builder()
                .setType(ErrorTypes.TRY_AGAIN_ERROR)
                .setTryAgainError(new TryAgainError("Internal error, try again"))
                .build();
        return handleExceptionInternal(ex, message, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}

