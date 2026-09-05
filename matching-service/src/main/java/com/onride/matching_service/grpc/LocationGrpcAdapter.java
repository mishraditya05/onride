package com.onride.matching_service.grpc;

import com.onride.location.grpc.v1.DriverInfo;
import com.onride.location.grpc.v1.DriversInCellResponse;
import com.onride.matching_service.dto.AvailableDriverDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class LocationGrpcAdapter {

    public List<AvailableDriverDto> toAvailableDriverDtoList(DriversInCellResponse response) {
        return response.getDriversList().stream()
                .map(this::toAvailableDriverDto)
                .toList();
    }

    private AvailableDriverDto toAvailableDriverDto(DriverInfo driver) {
        return new AvailableDriverDto(
                UUID.fromString(driver.getDriverId()),
                driver.getLat(),
                driver.getLng(),
                driver.getLastSeenAt());
    }
}