package com.onride.common.web.error;

public enum CommonErrorCode implements ErrorCode {

    VALIDATION_FAILED,
    MALFORMED_REQUEST,
    RESOURCE_NOT_FOUND,
    INTERNAL_ERROR;

    @Override
    public String code() {
        return name();
    }
}