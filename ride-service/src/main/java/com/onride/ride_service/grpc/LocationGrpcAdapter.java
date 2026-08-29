package com.onride.ride_service.grpc;

import com.onride.location.grpc.v1.DriverPosition;
import com.onride.location.grpc.v1.NearbyDriversResponse;
import com.onride.ride_service.client.dto.NearbyDriverDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class LocationGrpcAdapter {

    public List<NearbyDriverDto> toNearbyDriverDtoList(NearbyDriversResponse response) {
        return response.getDriversList().stream()
                .map(this::toNearbyDriverDto)
                .toList();
    }

    private NearbyDriverDto toNearbyDriverDto(DriverPosition driver) {
        return new NearbyDriverDto(
                UUID.fromString(driver.getDriverId()),
                driver.getLat(),
                driver.getLng(),
                driver.getDistanceMetres(),
                driver.getLastSeenAt());
    }
}