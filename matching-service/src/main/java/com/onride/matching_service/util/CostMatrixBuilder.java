package com.onride.matching_service.util;

import com.onride.common.web.geo.GeoIndex;
import com.onride.matching_service.config.MatchingProperties;
import com.onride.matching_service.dto.AvailableDriverDto;
import com.onride.matching_service.dto.PendingRideRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CostMatrixBuilder {

    private final GeoIndex geoIndex;
    private final MatchingProperties properties;

    public long[][] build(List<PendingRideRequestDto> riders, List<AvailableDriverDto> drivers) {
        long[][] cost = new long[riders.size()][drivers.size()];

        for (int i = 0; i < riders.size(); i++) {
            PendingRideRequestDto rider = riders.get(i);
            for (int j = 0; j < drivers.size(); j++) {
                AvailableDriverDto driver = drivers.get(j);
                cost[i][j] = etaSeconds(rider, driver) - rider.waitPenaltySeconds();
            }
        }

        return cost;
    }

    private long etaSeconds(PendingRideRequestDto rider, AvailableDriverDto driver) {
        double distanceMetres = geoIndex.distanceMetres(rider.pickupLat(), rider.pickupLng(), driver.lat(), driver.lng());
        double metresPerSecond = properties.averageSpeedKmph() * 1000.0 / 3600.0;
        return Math.round(distanceMetres / metresPerSecond);
    }
}