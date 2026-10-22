package com.onride.assistant_service.exception;

import com.onride.common.web.error.ErrorCode;

public enum AssistantErrorCode implements ErrorCode {

    CHAT_SESSION_CLOSED;

    @Override
    public String code() {
        return name();
    }
}