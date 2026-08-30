package com.onride.location_service.grpc;

import com.onride.location.grpc.v1.DriversInCellRequest;
import com.onride.location.grpc.v1.DriversInCellResponse;
import com.onride.location.grpc.v1.LocationServiceGrpc;
import com.onride.location.grpc.v1.NearbyDriversRequest;
import com.onride.location.grpc.v1.NearbyDriversResponse;
import com.onride.location_service.service.LocationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class LocationGrpcService extends LocationServiceGrpc.LocationServiceImplBase {

    private final LocationService locationService;
    private final LocationGrpcAdapter locationGrpcAdapter;

    @Override
    public void getNearbyDrivers(NearbyDriversRequest request, StreamObserver<NearbyDriversResponse> responseObserver) {
        var drivers = locationService.findNearby(request.getLat(), request.getLng(), request.getLimit());
        responseObserver.onNext(locationGrpcAdapter.toNearbyDriversResponse(drivers));
        responseObserver.onCompleted();
    }

    @Override
    public void getDriversInCell(DriversInCellRequest request, StreamObserver<DriversInCellResponse> responseObserver) {
        var drivers = locationService.findDriversInCell(request.getCellId());
        responseObserver.onNext(locationGrpcAdapter.toDriversInCellResponse(drivers));
        responseObserver.onCompleted();
    }
}