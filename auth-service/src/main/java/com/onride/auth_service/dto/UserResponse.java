package com.onride.auth_service.dto;

import com.onride.auth_service.enums.AccountStatus;
import com.onride.auth_service.enums.Role;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        Role role,
        AccountStatus status,
        Instant createdAt
) {
}
