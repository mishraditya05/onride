package com.onride.matching_service.service;

import com.onride.matching_service.config.MatchingProperties;
import com.onride.matching_service.dto.AvailableDriverDto;
import com.onride.matching_service.dto.MatchedRideDto;
import com.onride.matching_service.dto.PendingRideRequestDto;
import com.onride.matching_service.grpc.LocationGrpcClient;
import com.onride.matching_service.redis.MatchedRideRequestStore;
import com.onride.matching_service.redis.PendingRideRequestStore;
import com.onride.matching_service.util.CostMatrixBuilder;
import com.onride.matching_service.util.HungarianMatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingService {

    private final PendingRideRequestStore store;
    private final MatchedRideRequestStore matchedRideRequestStore;
    private final LocationGrpcClient locationGrpcClient;
    private final CostMatrixBuilder costMatrixBuilder;
    private final HungarianMatcher hungarianMatcher;
    private final MatchingProperties properties;
    private final Clock clock;

    public void runMatchingCycle() {
        sweepExpiredOffers();

        Map<String, List<PendingRideRequestDto>> pendingByCell = store.findAndDeleteAll();
        Set<UUID> lockedDriverIds = matchedRideRequestStore.findLockedDriverIds();

        for (Map.Entry<String, List<PendingRideRequestDto>> entry : pendingByCell.entrySet()) {
            matchCell(entry.getKey(), entry.getValue(), lockedDriverIds);
        }
    }

    private void sweepExpiredOffers() {
        List<MatchedRideDto> expired = matchedRideRequestStore.findAndDeleteExpired(clock.millis());

        for (MatchedRideDto match : expired) {
            log.debug("Offer {} for ride {} expired without driver response, requeuing",
                    match.matchId(), match.rider().rideId());
            requeue(match.rider());
        }
    }

    private void matchCell(String cellId, List<PendingRideRequestDto> riders, Set<UUID> lockedDriverIds) {
        List<AvailableDriverDto> drivers = locationGrpcClient.findDriversInCell(cellId).stream()
                .filter(driver -> !lockedDriverIds.contains(driver.driverId()))
                .toList();

        long[][] cost = costMatrixBuilder.build(riders, drivers);
        HungarianMatcher.MatchResult result = hungarianMatcher.match(riders.size(), drivers.size(), cost);

        for (HungarianMatcher.Match match : result.matches()) {
            PendingRideRequestDto rider = riders.get(match.riderIndex());
            AvailableDriverDto driver = drivers.get(match.driverIndex());
            lockMatch(rider, driver, cellId);
        }

        for (int riderIndex : result.unmatchedRiderIndexes()) {
            requeue(riders.get(riderIndex));
        }
    }

    private void lockMatch(PendingRideRequestDto rider, AvailableDriverDto driver, String cellId) {
        UUID matchId = UUID.randomUUID();
        long expiresAt = clock.millis() + Duration.ofSeconds(properties.offerWindowSeconds()).toMillis();
        MatchedRideDto match = new MatchedRideDto(matchId, expiresAt, rider);

        boolean locked = matchedRideRequestStore.tryLock(driver.driverId(), match);
        if (!locked) {
            log.debug("Driver {} already locked, requeuing ride {} in cell {}",
                    driver.driverId(), rider.rideId(), cellId);
            requeue(rider);
            return;
        }

        log.info("Matched ride {} (rider {}) to driver {} in cell {} with matchId {}",
                rider.rideId(), rider.riderId(), driver.driverId(), cellId, matchId);
    }

    private void requeue(PendingRideRequestDto rider) {
        long penaltyIncrementSeconds = properties.batchIntervalMs() / 1000L;

        PendingRideRequestDto requeued = new PendingRideRequestDto(
                rider.rideId(),
                rider.riderId(),
                rider.pickupLat(),
                rider.pickupLng(),
                rider.pickupGeoCell(),
                rider.requestedAt(),
                rider.waitPenaltySeconds() + penaltyIncrementSeconds);

        store.save(requeued);
        log.debug("No driver available for ride {} in cell {}, requeued with wait penalty {}s",
                rider.rideId(), rider.pickupGeoCell(), requeued.waitPenaltySeconds());
    }
}