package com.onride.matching_service.scheduler;

import com.onride.matching_service.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchingScheduler {

    private final MatchingService matchingService;

    @Scheduled(fixedRateString = "${onride.matching.batch-interval-ms}")
    public void runMatchingCycle() {
        matchingService.runMatchingCycle();
    }
}