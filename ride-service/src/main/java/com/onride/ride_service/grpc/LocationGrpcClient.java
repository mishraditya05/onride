package com.onride.ride_service.grpc;

import com.onride.location.grpc.v1.LocationServiceGrpc;
import com.onride.location.grpc.v1.NearbyDriversRequest;
import com.onride.location.grpc.v1.NearbyDriversResponse;
import com.onride.ride_service.client.dto.NearbyDriverDto;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LocationGrpcClient {

    private final LocationGrpcAdapter locationGrpcAdapter;

    @GrpcClient("location-service")
    private LocationServiceGrpc.LocationServiceBlockingStub locationServiceStub;

    public List<NearbyDriverDto> findNearbyDrivers(double lat, double lng, int limit) {
        NearbyDriversRequest request = NearbyDriversRequest.newBuilder()
                .setLat(lat)
                .setLng(lng)
                .setLimit(limit)
                .build();

        NearbyDriversResponse response = locationServiceStub.getNearbyDrivers(request);

        return locationGrpcAdapter.toNearbyDriverDtoList(response);
    }
}