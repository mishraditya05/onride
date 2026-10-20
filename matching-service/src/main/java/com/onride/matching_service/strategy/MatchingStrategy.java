package com.onride.matching_service.strategy;

import com.onride.matching_service.dto.AvailableDriverDto;
import com.onride.matching_service.dto.PendingRideRequestDto;
import com.onride.matching_service.enums.MatchingMode;

import java.util.List;

/**
 * Decides which riders are offered to which drivers within a single geo cell.
 *
 * <p>A strategy only decides pairings. Locking offers in Redis and requeuing riders
 * that could not be served stays in {@code MatchingService}, so that every mode shares
 * one implementation of those side effects.
 */
public interface MatchingStrategy {

    MatchingMode mode();

    Result match(List<PendingRideRequestDto> riders, List<AvailableDriverDto> drivers);

    /** A single rider-to-driver offer. One rider may appear in several offers under BROADCAST. */
    record Offer(PendingRideRequestDto rider, AvailableDriverDto driver) {
    }

    /**
     * @param offers          offers to attempt, in the order the strategy prefers them
     * @param unmatchedRiders riders the strategy could not offer to anyone
     */
    record Result(List<Offer> offers, List<PendingRideRequestDto> unmatchedRiders) {
    }
}
