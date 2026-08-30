package com.onride.auth_service.controller;

import com.onride.auth_service.dto.AuthResponseDto;
import com.onride.auth_service.dto.LoginRequestDto;
import com.onride.auth_service.dto.SignupRequestDto;
import com.onride.auth_service.dto.UserResponseDto;
import com.onride.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public UserResponseDto signup(@Valid @RequestBody SignupRequestDto request) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody LoginRequestDto request) {
        return authService.login(request);
    }

    @GetMapping("/users/{id}")
    public UserResponseDto getUser(@PathVariable UUID id) {
        return authService.getUser(id);
    }
}
