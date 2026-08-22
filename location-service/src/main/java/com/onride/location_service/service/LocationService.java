package com.onride.location_service.service;

import com.onride.location_service.dto.LocationPingRequestDto;
import com.onride.location_service.dto.LocationPingResponseDto;
import com.onride.location_service.redis.DriverLocationStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final H3IndexService h3Index;
    private final DriverLocationStore store;
    private final Clock clock;

    public LocationPingResponseDto recordPing(UUID driverId, LocationPingRequestDto request) {
        long now = clock.millis();
        String cellId = h3Index.toCell(request.lat(), request.lng());

        boolean changedCell = store.savePosition(driverId, request.lat(), request.lng(), cellId, now);

        return new LocationPingResponseDto(cellId, now, changedCell);
    }
}