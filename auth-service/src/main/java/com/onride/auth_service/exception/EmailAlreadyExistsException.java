package com.onride.auth_service.exception;

import com.onride.common.web.error.ApiException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends ApiException {

    public EmailAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, AuthErrorCode.EMAIL_ALREADY_EXISTS, message);
    }
}