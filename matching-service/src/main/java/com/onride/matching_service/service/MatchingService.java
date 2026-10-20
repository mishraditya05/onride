package com.onride.matching_service.service;

import com.onride.matching_service.config.MatchingProperties;
import com.onride.matching_service.dto.AvailableDriverDto;
import com.onride.matching_service.dto.MatchedRideDto;
import com.onride.matching_service.dto.PendingRideRequestDto;
import com.onride.matching_service.enums.MatchingMode;
import com.onride.matching_service.grpc.LocationGrpcClient;
import com.onride.matching_service.redis.MatchedRideRequestStore;
import com.onride.matching_service.redis.PendingRideRequestStore;
import com.onride.matching_service.strategy.MatchingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingService {

    private final PendingRideRequestStore store;
    private final MatchedRideRequestStore matchedRideRequestStore;
    private final LocationGrpcClient locationGrpcClient;
    private final Map<MatchingMode, MatchingStrategy> strategies;
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

        // A BROADCAST ride has one offer per driver, so dedupe by ride before requeuing or the
        // pending queue gains a copy of the ride for every offer that went unanswered.
        Map<UUID, MatchedRideDto> expiredByRide = expired.stream()
                .collect(Collectors.toMap(
                        match -> match.rider().rideId(),
                        Function.identity(),
                        (first, ignored) -> first,
                        LinkedHashMap::new));

        for (MatchedRideDto match : expiredByRide.values()) {
            log.debug("Offer(s) for ride {} expired without driver response, requeuing",
                    match.rider().rideId());
            requeue(match.rider());
        }
    }

    private void matchCell(String cellId, List<PendingRideRequestDto> riders, Set<UUID> lockedDriverIds) {
        List<AvailableDriverDto> drivers = locationGrpcClient.findDriversInCell(cellId).stream()
                .filter(driver -> !lockedDriverIds.contains(driver.driverId()))
                .toList();

        Map<MatchingMode, List<PendingRideRequestDto>> ridersByMode = riders.stream()
                .collect(Collectors.groupingBy(
                        MatchingService::modeOf,
                        () -> new EnumMap<>(MatchingMode.class),
                        Collectors.toList()));

        // Urgent riders claim free drivers first. Everyone else requeues with the wait penalty
        // that already exists, so a busy cell delays them by a cycle rather than starving them.
        List<Map.Entry<MatchingMode, List<PendingRideRequestDto>>> byUrgency = ridersByMode.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(entry -> entry.getKey() == MatchingMode.BROADCAST ? 0 : 1))
                .toList();

        for (Map.Entry<MatchingMode, List<PendingRideRequestDto>> entry : byUrgency) {
            MatchingStrategy strategy = strategies.get(entry.getKey());
            if (strategy == null) {
                log.error("No matching strategy registered for mode {}, requeuing {} rider(s) in cell {}",
                        entry.getKey(), entry.getValue().size(), cellId);
                entry.getValue().forEach(this::requeue);
                continue;
            }

            dispatchOffers(cellId, strategy.match(entry.getValue(), drivers));
        }
    }

    /**
     * Applies whatever the strategy decided. A rider is only requeued if none of its offers
     * could be locked, so a broadcast rider losing some drivers to a concurrent lock still
     * counts as served.
     */
    private void dispatchOffers(String cellId, MatchingStrategy.Result result) {
        Map<UUID, List<MatchingStrategy.Offer>> offersByRide = result.offers().stream()
                .collect(Collectors.groupingBy(
                        offer -> offer.rider().rideId(), LinkedHashMap::new, Collectors.toList()));

        for (List<MatchingStrategy.Offer> offers : offersByRide.values()) {
            boolean anyLocked = false;
            for (MatchingStrategy.Offer offer : offers) {
                anyLocked |= lockMatch(offer.rider(), offer.driver(), cellId);
            }

            if (!anyLocked) {
                requeue(offers.getFirst().rider());
            }
        }

        for (PendingRideRequestDto rider : result.unmatchedRiders()) {
            requeue(rider);
        }
    }

    private boolean lockMatch(PendingRideRequestDto rider, AvailableDriverDto driver, String cellId) {
        UUID matchId = UUID.randomUUID();
        long expiresAt = clock.millis() + Duration.ofSeconds(properties.offerWindowSeconds()).toMillis();
        MatchedRideDto match = new MatchedRideDto(matchId, expiresAt, rider);

        boolean locked = matchedRideRequestStore.tryLock(driver.driverId(), match);
        if (!locked) {
            log.debug("Driver {} already locked, skipping offer for ride {} in cell {}",
                    driver.driverId(), rider.rideId(), cellId);
            return false;
        }

        log.info("Offered ride {} (rider {}) to driver {} in cell {} with matchId {} in {} mode",
                rider.rideId(), rider.riderId(), driver.driverId(), cellId, matchId, modeOf(rider));
        return true;
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
                rider.waitPenaltySeconds() + penaltyIncrementSeconds,
                modeOf(rider));

        store.save(requeued);
        log.debug("No driver available for ride {} in cell {}, requeued with wait penalty {}s",
                rider.rideId(), rider.pickupGeoCell(), requeued.waitPenaltySeconds());
    }

    /** Defaults to DIRECT so requests buffered before matchingMode existed still match. */
    private static MatchingMode modeOf(PendingRideRequestDto rider) {
        return rider.matchingMode() == null ? MatchingMode.DIRECT : rider.matchingMode();
    }
}
