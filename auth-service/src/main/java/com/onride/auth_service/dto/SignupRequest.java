package com.onride.auth_service.dto;

import com.onride.auth_service.enums.Role;

public record SignupRequest(String email, String password, Role role) {
}
