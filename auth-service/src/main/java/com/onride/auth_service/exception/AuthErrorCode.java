package com.onride.auth_service.exception;

import com.onride.common.web.error.ErrorCode;

public enum AuthErrorCode implements ErrorCode {

    EMAIL_ALREADY_EXISTS,
    INVALID_CREDENTIALS;

    @Override
    public String code() {
        return name();
    }
}