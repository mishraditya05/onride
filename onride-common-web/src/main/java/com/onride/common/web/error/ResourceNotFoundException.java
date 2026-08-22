package com.onride.common.web.error;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, CommonErrorCode.RESOURCE_NOT_FOUND, message);
    }
}