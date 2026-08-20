package com.onride.auth_service.controller;

import com.onride.auth_service.dto.CreateVehicleRequestDto;
import com.onride.auth_service.dto.DriverResponseDto;
import com.onride.auth_service.dto.UpdateDriverRequestDto;
import com.onride.auth_service.dto.VehicleResponseDto;
import com.onride.auth_service.service.DriverService;
import com.onride.auth_service.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final VehicleService vehicleService;

    @GetMapping("/me")
    public DriverResponseDto getMe(@RequestHeader("X-User-Id") UUID userId) {
        return driverService.getByUserId(userId);
    }

    @PutMapping("/me")
    public DriverResponseDto updateMe(@RequestHeader("X-User-Id") UUID userId,
                                      @Valid @RequestBody UpdateDriverRequestDto request) {
        return driverService.updateProfile(userId, request);
    }

    @PostMapping("/me/vehicles")
    public VehicleResponseDto addVehicle(@RequestHeader("X-User-Id") UUID userId,
                                         @Valid @RequestBody CreateVehicleRequestDto request) {
        return vehicleService.addVehicle(userId, request);
    }

    @GetMapping("/me/vehicles")
    public List<VehicleResponseDto> listMyVehicles(@RequestHeader("X-User-Id") UUID userId) {
        return vehicleService.listMyVehicles(userId);
    }
}