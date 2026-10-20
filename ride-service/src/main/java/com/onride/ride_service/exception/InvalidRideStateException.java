package com.onride.ride_service.exception;

import com.onride.common.web.error.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidRideStateException extends ApiException {

    public InvalidRideStateException(String message) {
        super(HttpStatus.CONFLICT, RideErrorCode.INVALID_RIDE_STATE, message);
    }
}
