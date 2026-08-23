package com.onride.ride_service.service;

import com.onride.ride_service.enums.VehicleType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Map;

@Component
public class FareCalculator {

    private record Rate(BigDecimal base, BigDecimal perKm, BigDecimal perMinute, BigDecimal minimum) {
    }

    private static final Map<VehicleType, Rate> RATES = new EnumMap<>(Map.of(
            VehicleType.BIKE, rate(15, 6, 1.0, 25),
            VehicleType.AUTO, rate(25, 11, 1.5, 40),
            VehicleType.SEDAN, rate(50, 16, 2.0, 80),
            VehicleType.SUV, rate(70, 22, 2.5, 110)
    ));

    public BigDecimal fareFor(VehicleType vehicleType, long distanceMetres, long durationSeconds) {
        Rate rate = RATES.get(vehicleType);

        BigDecimal km = BigDecimal.valueOf(distanceMetres).divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
        BigDecimal minutes = BigDecimal.valueOf(durationSeconds).divide(BigDecimal.valueOf(60), 3, RoundingMode.HALF_UP);

        BigDecimal fare = rate.base()
                .add(rate.perKm().multiply(km))
                .add(rate.perMinute().multiply(minutes));

        return fare.max(rate.minimum()).setScale(2, RoundingMode.HALF_UP);
    }

    private static Rate rate(double base, double perKm, double perMinute, double minimum) {
        return new Rate(
                BigDecimal.valueOf(base),
                BigDecimal.valueOf(perKm),
                BigDecimal.valueOf(perMinute),
                BigDecimal.valueOf(minimum));
    }
}