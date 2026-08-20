package com.onride.auth_service.controller;

import com.onride.auth_service.dto.RiderResponseDto;
import com.onride.auth_service.dto.UpdateRiderRequestDto;
import com.onride.auth_service.service.RiderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/riders")
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @GetMapping("/me")
    public RiderResponseDto getMe(@RequestHeader("X-User-Id") UUID userId) {
        return riderService.getByUserId(userId);
    }

    @PutMapping("/me")
    public RiderResponseDto updateMe(@RequestHeader("X-User-Id") UUID userId,
                                  @Valid @RequestBody UpdateRiderRequestDto request) {
        return riderService.updateProfile(userId, request);
    }
}