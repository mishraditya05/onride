package com.onride.auth_service.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApiException {

    public InvalidCredentialsException(String message) {
        super(HttpStatus.UNAUTHORIZED, ErrorCode.INVALID_CREDENTIALS, message);
    }
}