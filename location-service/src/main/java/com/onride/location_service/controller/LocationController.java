package com.onride.location_service.controller;

import com.onride.location_service.dto.LocationPingRequestDto;
import com.onride.location_service.dto.LocationPingResponseDto;
import com.onride.location_service.dto.NearbyDriverDto;
import com.onride.location_service.service.LocationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Validated
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

    @GetMapping("/nearby")
    public List<NearbyDriverDto> nearby(
            @RequestParam
            @DecimalMin(value = "-90.0", message = "lat must be between -90 and 90")
            @DecimalMax(value = "90.0", message = "lat must be between -90 and 90")
            double lat,

            @RequestParam
            @DecimalMin(value = "-180.0", message = "lng must be between -180 and 180")
            @DecimalMax(value = "180.0", message = "lng must be between -180 and 180")
            double lng,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "limit must be between 1 and 50")
            @Max(value = 50, message = "limit must be between 1 and 50")
            int limit) {

        return locationService.findNearby(lat, lng, limit);
    }
}