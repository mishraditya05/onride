package com.onride.matching_service.grpc;

import com.onride.location.grpc.v1.DriversInCellRequest;
import com.onride.location.grpc.v1.DriversInCellResponse;
import com.onride.location.grpc.v1.LocationServiceGrpc;
import com.onride.matching_service.dto.AvailableDriverDto;
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

    public List<AvailableDriverDto> findDriversInCell(String cellId) {
        DriversInCellRequest request = DriversInCellRequest.newBuilder()
                .setCellId(cellId)
                .build();

        DriversInCellResponse response = locationServiceStub.getDriversInCell(request);

        return locationGrpcAdapter.toAvailableDriverDtoList(response);
    }
}