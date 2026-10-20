package com.onride.matching_service.strategy;

import com.onride.matching_service.dto.AvailableDriverDto;
import com.onride.matching_service.dto.PendingRideRequestDto;
import com.onride.matching_service.enums.MatchingMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Urgent mode: offer the ride to every available driver in the cell at once and let the
 * first one to accept win, instead of optimising who gets whom.
 *
 * <p>Fan-out needs no new storage: offers are keyed by driver, so several drivers can
 * hold an offer for the same ride. Losing offers fall away on their existing expiry
 * sweep, and {@code acceptMatch} in ride-service rejects a second accept for a ride that
 * is no longer REQUESTED.
 *
 * <p>Deliberately thin: it does not cap the fan-out or cancel sibling offers eagerly.
 */
@Component
public class BroadcastMatchingStrategy implements MatchingStrategy {

    @Override
    public MatchingMode mode() {
        return MatchingMode.BROADCAST;
    }

    @Override
    public Result match(List<PendingRideRequestDto> riders, List<AvailableDriverDto> drivers) {
        List<Offer> offers = new ArrayList<>();
        List<PendingRideRequestDto> unmatched = new ArrayList<>();

        for (PendingRideRequestDto rider : riders) {
            if (drivers.isEmpty()) {
                unmatched.add(rider);
                continue;
            }

            for (AvailableDriverDto driver : drivers) {
                offers.add(new Offer(rider, driver));
            }
        }

        return new Result(offers, unmatched);
    }
}
