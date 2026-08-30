package com.onride.location_service.grpc;

import com.onride.location.grpc.v1.DriverInfo;
import com.onride.location.grpc.v1.DriverPosition;
import com.onride.location.grpc.v1.DriversInCellResponse;
import com.onride.location.grpc.v1.NearbyDriversResponse;
import com.onride.location_service.dto.DriverInCellDto;
import com.onride.location_service.dto.NearbyDriverDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationGrpcAdapter {

    public NearbyDriversResponse toNearbyDriversResponse(List<NearbyDriverDto> drivers) {
        NearbyDriversResponse.Builder response = NearbyDriversResponse.newBuilder();

        for (NearbyDriverDto driver : drivers) {
            response.addDrivers(toDriverPosition(driver));
        }

        return response.build();
    }

    private DriverPosition toDriverPosition(NearbyDriverDto driver) {
        return DriverPosition.newBuilder()
                .setDriverId(driver.driverId().toString())
                .setLat(driver.lat())
                .setLng(driver.lng())
                .setDistanceMetres(driver.distanceMetres())
                .setLastSeenAt(driver.lastSeenAt())
                .build();
    }

    public DriversInCellResponse toDriversInCellResponse(List<DriverInCellDto> drivers) {
        DriversInCellResponse.Builder response = DriversInCellResponse.newBuilder();

        for (DriverInCellDto driver : drivers) {
            response.addDrivers(toDriverInfo(driver));
        }

        return response.build();
    }

    private DriverInfo toDriverInfo(DriverInCellDto driver) {
        return DriverInfo.newBuilder()
                .setDriverId(driver.driverId().toString())
                .setLat(driver.lat())
                .setLng(driver.lng())
                .setLastSeenAt(driver.lastSeenAt())
                .build();
    }
}