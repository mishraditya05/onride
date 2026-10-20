package com.onride.ride_service.exception;

import com.onride.common.web.error.ErrorCode;

public enum RideErrorCode implements ErrorCode {

    INVALID_RIDE_STATE;

    @Override
    public String code() {
        return name();
    }
}
