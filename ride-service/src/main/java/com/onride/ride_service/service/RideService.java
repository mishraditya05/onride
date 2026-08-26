package com.onride.ride_service.service;

import com.onride.common.web.error.ResourceNotFoundException;
import com.onride.common.web.geo.GeoIndex;
import com.onride.ride_service.dto.BookRideRequestDto;
import com.onride.ride_service.dto.BookRideResponseDto;
import com.onride.ride_service.entity.Ride;
import com.onride.ride_service.enums.RideStatus;
import com.onride.ride_service.enums.VehicleType;
import com.onride.ride_service.mapper.RideMapper;
import com.onride.ride_service.redis.Quote;
import com.onride.ride_service.redis.QuoteStore;
import com.onride.ride_service.repository.RideRepository;
import lombok.RequiredArgsConstructor;
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

        return rideMapper.toBookRideResponseDto(ride);
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