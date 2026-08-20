package com.onride.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UpdateRiderRequestDto(

        @NotBlank(message = "firstName must not be blank")
        String firstName,

        @NotBlank(message = "lastName must not be blank")
        String lastName,

        @NotNull(message = "dateOfBirth must not be null")
        @Past(message = "dateOfBirth must be in the past")
        LocalDate dateOfBirth

) {
}