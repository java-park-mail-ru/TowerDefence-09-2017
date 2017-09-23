package com.td.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.td.errors.ErrorMessage;
import com.td.errors.ErrorTypes;
import com.td.errors.IncorrectRequestDataError;
import com.td.errors.views.ErrorViews;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @JsonView(ErrorViews.IncorrectRequestData.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMethodArgumentNotValid(MethodArgumentNotValidException exception) throws JsonProcessingException {
        List<IncorrectRequestDataError> errors = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError ->
                        new IncorrectRequestDataError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        return ErrorMessage
                .builder()
                .setType(ErrorTypes.INCORRECT_REQUEST_DATA)
                .setIncorrectRequestDataErrors(errors)
                .build();

    }
}
