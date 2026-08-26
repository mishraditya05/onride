package com.onride.location_service.service;

import com.onride.common.web.geo.GeoIndex;
import com.onride.location_service.config.LocationProperties;
import com.onride.location_service.dto.LocationPingRequestDto;
import com.onride.location_service.dto.LocationPingResponseDto;
import com.onride.location_service.dto.NearbyDriverDto;
import com.onride.location_service.redis.DriverLocationStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final GeoIndex geoIndex;
    private final DriverLocationStore store;
    private final LocationProperties properties;
    private final Clock clock;

    public LocationPingResponseDto recordPing(UUID driverId, LocationPingRequestDto request) {
        long now = clock.millis();
        String cellId = geoIndex.toCell(request.lat(), request.lng());

        boolean changedCell = store.savePosition(driverId, request.lat(), request.lng(), cellId, now);

        return new LocationPingResponseDto(cellId, now, changedCell);
    }

    public List<NearbyDriverDto> findNearby(double lat, double lng, int limit) {
        List<String> cells = geoIndex.neighbours(geoIndex.toCell(lat, lng), properties.searchRings());
        long freshSince = clock.millis() - properties.staleAfter().toMillis();

        Set<String> candidates = store.findFreshDriverIds(cells, freshSince);
        if (candidates.isEmpty()) {
            return List.of();
        }

        return store.findPositions(candidates).stream()
                .map(position -> new NearbyDriverDto(
                        position.driverId(),
                        position.lat(),
                        position.lng(),
                        Math.round(geoIndex.distanceMetres(lat, lng, position.lat(), position.lng())),
                        position.timestamp()))
                .sorted(Comparator.comparingLong(NearbyDriverDto::distanceMetres))
                .limit(limit)
                .toList();
    }
}