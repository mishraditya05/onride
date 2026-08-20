package com.onride.auth_service.dto;

import com.onride.auth_service.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignupRequestDto(

        @NotBlank(message = "email must not be blank")
        @Email(message = "email must be a valid email address")
        String email,

        @NotBlank(message = "password must not be blank")
        @Size(min = 8, message = "password must be at least 8 characters")
        String password,

        @NotNull(message = "role must not be null")
        Role role

) {
}
