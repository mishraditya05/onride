package com.onride.assistant_service.exception;

import com.onride.common.web.error.ApiException;
import org.springframework.http.HttpStatus;

public class ChatSessionClosedException extends ApiException {

    public ChatSessionClosedException(String message) {
        super(HttpStatus.CONFLICT, AssistantErrorCode.CHAT_SESSION_CLOSED, message);
    }
}