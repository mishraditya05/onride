package com.onride.ride_service.service;

import com.onride.ride_service.client.LocationClient;
import com.onride.ride_service.client.dto.NearbyDriverDto;
import com.onride.ride_service.config.PricingProperties;
import com.onride.ride_service.dto.QuoteRequestDto;
import com.onride.ride_service.dto.QuoteResponseDto;
import com.onride.ride_service.dto.VehicleQuoteDto;
import com.onride.ride_service.enums.VehicleType;
import com.onride.ride_service.util.GeoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuoteService {

    private static final int NEARBY_LIMIT = 10;
    private static final String CURRENCY = "INR";

    private final LocationClient locationClient;
    private final FareCalculator fareCalculator;
    private final PricingProperties pricing;

    public QuoteResponseDto getQuotes(QuoteRequestDto request) {
        long distanceMetres = Math.round(GeoUtils.haversineMetres(
                request.pickupLat(), request.pickupLng(),
                request.dropLat(), request.dropLng()) * pricing.roadFactor());

        long tripDurationSeconds = secondsToCover(distanceMetres);

        List<NearbyDriverDto> drivers = findNearbyDrivers(request.pickupLat(), request.pickupLng());

        Map<VehicleType, VehicleQuoteDto> vehicle = new EnumMap<>(VehicleType.class);
        for (VehicleType vehicleType : VehicleType.values()) {
            vehicle.put(vehicleType, new VehicleQuoteDto(
                    fareCalculator.fareFor(vehicleType, distanceMetres, tripDurationSeconds),
                    drivers.size()));
        }

        return new QuoteResponseDto(CURRENCY, distanceMetres, vehicle);
    }

    private List<NearbyDriverDto> findNearbyDrivers(double lat, double lng) {
        try {
            return locationClient.findNearbyDrivers(lat, lng, NEARBY_LIMIT);
        } catch (Exception ex) {
            log.warn("location-service unavailable, quoting without driver counts", ex);
            return List.of();
        }
    }

    private long secondsToCover(long metres) {
        double metresPerSecond = pricing.averageSpeedKmph() * 1000.0 / 3600.0;
        return Math.round(metres / metresPerSecond);
    }
}