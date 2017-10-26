package com.td.exceptions;

import com.td.dtos.errors.AuthorizationError;
import org.springframework.http.HttpStatus;

public class AuthException extends RuntimeException {
    private AuthorizationError error;
    private HttpStatus status;

    public AuthException(String message, String path, HttpStatus status) {
        super(message);
        this.error = new AuthorizationError(message, path);
        this.status = status;
    }

    public AuthorizationError getError() {
        return error;
    }

    public void setError(AuthorizationError error) {
        this.error = error;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}

