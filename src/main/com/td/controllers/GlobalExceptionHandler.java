package com.td.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.td.dtos.errors.ErrorMessage;
import com.td.dtos.errors.ErrorTypes;
import com.td.dtos.errors.IncorrectRequestDataError;
import com.td.dtos.errors.views.ErrorViews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalResponseEntityExceptionHandler.class);

    @ExceptionHandler
    @JsonView(ErrorViews.IncorrectRequestData.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMethodArgumentNotValid(MethodArgumentNotValidException exception) throws JsonProcessingException {
        List<IncorrectRequestDataError> errors = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> {
                    log.error("Field {}: {} {}", fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
                    return new IncorrectRequestDataError(fieldError.getField(), fieldError.getDefaultMessage());
                })
                .collect(Collectors.toList());

        log.error("Invalid object comes to controller ", exception);
        return ErrorMessage
                .builder()
                .setType(ErrorTypes.INCORRECT_REQUEST_DATA)
                .setIncorrectRequestDataErrors(errors)
                .build();
    }
}
