package com.onride.auth_service.exception;

import com.onride.common.web.error.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApiException {

    public InvalidCredentialsException(String message) {
        super(HttpStatus.UNAUTHORIZED, AuthErrorCode.INVALID_CREDENTIALS, message);
    }
}