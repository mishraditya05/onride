package com.onride.location_service.grpc;

import com.onride.location.grpc.v1.DriverPosition;
import com.onride.location.grpc.v1.LocationServiceGrpc;
import com.onride.location.grpc.v1.NearbyDriversRequest;
import com.onride.location.grpc.v1.NearbyDriversResponse;
import com.onride.location_service.dto.NearbyDriverDto;
import com.onride.location_service.service.LocationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class LocationGrpcService extends LocationServiceGrpc.LocationServiceImplBase {

    private final LocationService locationService;

    @Override
    public void getNearbyDrivers(NearbyDriversRequest request, StreamObserver<NearbyDriversResponse> responseObserver) {
        NearbyDriversResponse.Builder response = NearbyDriversResponse.newBuilder();

        for (NearbyDriverDto driver : locationService.findNearby(request.getLat(), request.getLng(), request.getLimit())) {
            response.addDrivers(DriverPosition.newBuilder()
                    .setDriverId(driver.driverId().toString())
                    .setLat(driver.lat())
                    .setLng(driver.lng())
                    .setDistanceMetres(driver.distanceMetres())
                    .setLastSeenAt(driver.lastSeenAt())
                    .build());
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
