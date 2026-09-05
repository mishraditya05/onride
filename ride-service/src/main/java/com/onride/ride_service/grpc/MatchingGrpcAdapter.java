package com.onride.ride_service.grpc;

import com.onride.matching.grpc.v1.ConfirmMatchResponse;
import com.onride.matching.grpc.v1.GetMatchForDriverResponse;
import com.onride.ride_service.client.dto.MatchInfoDto;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class MatchingGrpcAdapter {

    public Optional<MatchInfoDto> toMatchInfoDto(GetMatchForDriverResponse response) {
        if (!response.getFound()) {
            return Optional.empty();
        }

        return Optional.of(new MatchInfoDto(
                UUID.fromString(response.getMatchId()),
                UUID.fromString(response.getRideId()),
                UUID.fromString(response.getRiderId())));
    }

    public Optional<MatchInfoDto> toMatchInfoDto(ConfirmMatchResponse response) {
        if (!response.getConfirmed()) {
            return Optional.empty();
        }

        return Optional.of(new MatchInfoDto(
                null,
                UUID.fromString(response.getRideId()),
                UUID.fromString(response.getRiderId())));
    }
}