package com.onride.matching_service.listener;

import com.onride.events.RideRequestedEvent;
import com.onride.matching_service.dto.PendingRideRequestDto;
import com.onride.matching_service.redis.PendingRideRequestStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RideRequestedListener {

    private final PendingRideRequestStore store;
    private final Clock clock;

    @KafkaListener(topics = "${kafka.topic.ride-requested-topic}")
    public void onRideRequested(RideRequestedEvent event) {
        PendingRideRequestDto request = new PendingRideRequestDto(
                UUID.fromString(event.getRideId()),
                UUID.fromString(event.getRiderId()),
                event.getPickupLat(),
                event.getPickupLng(),
                event.getPickupGeoCell(),
                clock.millis(),
                0L);

        store.save(request);
        log.debug("Buffered ride request {} for cell {}", request.rideId(), request.pickupGeoCell());
    }
}