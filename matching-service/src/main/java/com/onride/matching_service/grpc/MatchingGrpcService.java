package com.onride.matching_service.grpc;

import com.onride.matching.grpc.v1.ConfirmMatchRequest;
import com.onride.matching.grpc.v1.ConfirmMatchResponse;
import com.onride.matching.grpc.v1.GetMatchForDriverRequest;
import com.onride.matching.grpc.v1.GetMatchForDriverResponse;
import com.onride.matching.grpc.v1.MatchingServiceGrpc;
import com.onride.matching_service.dto.MatchedRideDto;
import com.onride.matching_service.redis.MatchedRideRequestStore;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Optional;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class MatchingGrpcService extends MatchingServiceGrpc.MatchingServiceImplBase {

    private final MatchedRideRequestStore matchedRideRequestStore;

    @Override
    public void getMatchForDriver(GetMatchForDriverRequest request, StreamObserver<GetMatchForDriverResponse> responseObserver) {
        Optional<MatchedRideDto> match = matchedRideRequestStore.find(UUID.fromString(request.getDriverId()));

        GetMatchForDriverResponse response = match
                .map(m -> GetMatchForDriverResponse.newBuilder()
                        .setFound(true)
                        .setMatchId(m.matchId().toString())
                        .setRideId(m.rideId().toString())
                        .setRiderId(m.riderId().toString())
                        .build())
                .orElseGet(() -> GetMatchForDriverResponse.newBuilder()
                        .setFound(false)
                        .build());

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void confirmMatch(ConfirmMatchRequest request, StreamObserver<ConfirmMatchResponse> responseObserver) {
        Optional<MatchedRideDto> match = matchedRideRequestStore.tryConfirm(
                UUID.fromString(request.getDriverId()),
                UUID.fromString(request.getMatchId()));

        ConfirmMatchResponse response = match
                .map(m -> ConfirmMatchResponse.newBuilder()
                        .setConfirmed(true)
                        .setRideId(m.rideId().toString())
                        .setRiderId(m.riderId().toString())
                        .build())
                .orElseGet(() -> ConfirmMatchResponse.newBuilder()
                        .setConfirmed(false)
                        .build());

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}