package com.onride.matching_service.strategy;

import com.onride.matching_service.dto.AvailableDriverDto;
import com.onride.matching_service.dto.PendingRideRequestDto;
import com.onride.matching_service.enums.MatchingMode;
import com.onride.matching_service.util.CostMatrixBuilder;
import com.onride.matching_service.util.HungarianMatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Default mode: solve the cell as an assignment problem so that total rider wait is
 * minimised, giving every rider at most one driver.
 */
@Component
@RequiredArgsConstructor
public class HungarianMatchingStrategy implements MatchingStrategy {

    private final CostMatrixBuilder costMatrixBuilder;
    private final HungarianMatcher hungarianMatcher;

    @Override
    public MatchingMode mode() {
        return MatchingMode.DIRECT;
    }

    @Override
    public Result match(List<PendingRideRequestDto> riders, List<AvailableDriverDto> drivers) {
        long[][] cost = costMatrixBuilder.build(riders, drivers);
        HungarianMatcher.MatchResult result = hungarianMatcher.match(riders.size(), drivers.size(), cost);

        List<Offer> offers = new ArrayList<>();
        for (HungarianMatcher.Match match : result.matches()) {
            offers.add(new Offer(riders.get(match.riderIndex()), drivers.get(match.driverIndex())));
        }

        List<PendingRideRequestDto> unmatched = new ArrayList<>();
        for (int riderIndex : result.unmatchedRiderIndexes()) {
            unmatched.add(riders.get(riderIndex));
        }

        return new Result(offers, unmatched);
    }
}
