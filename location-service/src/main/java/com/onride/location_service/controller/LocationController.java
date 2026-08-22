package com.onride.location_service.controller;

import com.onride.location_service.dto.LocationPingRequestDto;
import com.onride.location_service.dto.LocationPingResponseDto;
import com.onride.location_service.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/ping")
    public LocationPingResponseDto ping(@RequestHeader("X-Driver-Id") UUID driverId,
                                       @Valid @RequestBody LocationPingRequestDto request) {
        return locationService.recordPing(driverId, request);
    }
}