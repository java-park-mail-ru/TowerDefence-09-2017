package com.td.exceptions;

import com.td.errors.AuthorizationError;
import org.springframework.http.HttpStatus;

public class AuthException extends RuntimeException {
    private AuthorizationError error;
    private HttpStatus status;

    public AuthException(String message, String path, HttpStatus status) {
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
