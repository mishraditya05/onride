package com.onride.auth_service.dto;

public record AuthResponseDto(
        String accessToken,
        UserResponseDto user
) {
}