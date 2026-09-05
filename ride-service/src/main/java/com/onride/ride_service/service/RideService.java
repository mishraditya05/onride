package com.onride.ride_service.service;

import com.onride.common.web.error.ResourceNotFoundException;
import com.onride.common.web.geo.GeoIndex;
import com.onride.events.RideRequestedEvent;
import com.onride.ride_service.client.dto.MatchInfoDto;
import com.onride.ride_service.dto.AcceptMatchResponseDto;
import com.onride.ride_service.dto.BookRideRequestDto;
import com.onride.ride_service.dto.BookRideResponseDto;
import com.onride.ride_service.dto.PendingMatchResponseDto;
import com.onride.ride_service.entity.Ride;
import com.onride.ride_service.enums.RideStatus;
import com.onride.ride_service.enums.VehicleType;
import com.onride.ride_service.grpc.MatchingGrpcClient;
import com.onride.ride_service.mapper.RideEventMapper;
import com.onride.ride_service.mapper.RideMapper;
import com.onride.ride_service.redis.Quote;
import com.onride.ride_service.redis.QuoteStore;
import com.onride.ride_service.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RideService {

    private final QuoteStore quoteStore;
    private final GeoIndex geoIndex;
    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final RideEventMapper rideEventMapper;
    private final KafkaTemplate<String, RideRequestedEvent> kafkaTemplate;
    private final MatchingGrpcClient matchingGrpcClient;

    @Value("${kafka.topic.ride-requested-topic}")
    private String rideRequestedTopic;

    @Transactional
    public BookRideResponseDto book(UUID riderId, BookRideRequestDto request) {
        Quote quote = quoteStore.findAndDelete(riderId)
                .filter(q -> q.quoteId().equals(request.quoteId()))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Quote expired or invalid, please request a new quote"));

        BigDecimal fare = quote.fares().get(request.vehicleType());
        if (fare == null) {
            throw new ResourceNotFoundException(
                    "Quote expired or invalid, please request a new quote");
        }

        String pickupGeoCell = geoIndex.toCell(quote.pickupLat(), quote.pickupLng());

        Ride ride = buildRide(riderId, quote, request.vehicleType(), fare, pickupGeoCell);
        rideRepository.save(ride);

        RideRequestedEvent event = rideEventMapper.toRideRequestedEvent(ride);
        kafkaTemplate.send(rideRequestedTopic, pickupGeoCell, event);

        return rideMapper.toBookRideResponseDto(ride);
    }

    public PendingMatchResponseDto getPendingMatch(UUID driverId) {
        return matchingGrpcClient.getMatchForDriver(driverId)
                .map(match -> new PendingMatchResponseDto(true, match.matchId(), match.rideId(), match.riderId()))
                .orElseGet(() -> new PendingMatchResponseDto(false, null, null, null));
    }

    @Transactional
    public AcceptMatchResponseDto acceptMatch(UUID driverId, UUID rideId, UUID matchId) {
        MatchInfoDto match = matchingGrpcClient.confirmMatch(driverId, matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match expired or already confirmed"));

        if (!match.rideId().equals(rideId)) {
            throw new ResourceNotFoundException("Match does not correspond to this ride");
        }

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));

        ride.setDriverId(driverId);
        ride.setStatus(RideStatus.MATCHED);
        rideRepository.save(ride);

        return new AcceptMatchResponseDto(ride.getId(), ride.getStatus(), ride.getDriverId());
    }

    private static Ride buildRide(UUID riderId, Quote quote, VehicleType vehicleType,
                                   BigDecimal fare, String pickupGeoCell) {
        Ride ride = new Ride();
        ride.setRiderId(riderId);
        ride.setQuoteId(quote.quoteId());
        ride.setPickupLat(quote.pickupLat());
        ride.setPickupLng(quote.pickupLng());
        ride.setDropLat(quote.dropLat());
        ride.setDropLng(quote.dropLng());
        ride.setPickupGeoCell(pickupGeoCell);
        ride.setDistanceMetres(quote.distanceMetres());
        ride.setVehicleType(vehicleType);
        ride.setFare(fare);
        ride.setCurrency(quote.currency());
        ride.setStatus(RideStatus.REQUESTED);
        return ride;
    }
}