package com.onride.ride_service.grpc;

import com.onride.matching.grpc.v1.ConfirmMatchRequest;
import com.onride.matching.grpc.v1.ConfirmMatchResponse;
import com.onride.matching.grpc.v1.GetMatchForDriverRequest;
import com.onride.matching.grpc.v1.GetMatchForDriverResponse;
import com.onride.matching.grpc.v1.MatchingServiceGrpc;
import com.onride.ride_service.client.dto.MatchInfoDto;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MatchingGrpcClient {

    private final MatchingGrpcAdapter matchingGrpcAdapter;

    @GrpcClient("matching-service")
    private MatchingServiceGrpc.MatchingServiceBlockingStub matchingServiceStub;

    public Optional<MatchInfoDto> getMatchForDriver(UUID driverId) {
        GetMatchForDriverRequest request = GetMatchForDriverRequest.newBuilder()
                .setDriverId(driverId.toString())
                .build();

        GetMatchForDriverResponse response = matchingServiceStub.getMatchForDriver(request);
        return matchingGrpcAdapter.toMatchInfoDto(response);
    }

    public Optional<MatchInfoDto> confirmMatch(UUID driverId, UUID matchId) {
        ConfirmMatchRequest request = ConfirmMatchRequest.newBuilder()
                .setDriverId(driverId.toString())
                .setMatchId(matchId.toString())
                .build();

        ConfirmMatchResponse response = matchingServiceStub.confirmMatch(request);
        return matchingGrpcAdapter.toMatchInfoDto(response);
    }
}