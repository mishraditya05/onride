package com.onride.location_service.service;

import com.onride.location_service.config.LocationProperties;
import com.uber.h3core.H3Core;
import com.uber.h3core.LengthUnit;
import com.uber.h3core.util.LatLng;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class H3IndexService {

    private final H3Core h3;
    private final int resolution;

    public H3IndexService(H3Core h3, LocationProperties properties) {
        this.h3 = h3;
        this.resolution = properties.h3Resolution();
    }

    public String toCell(double lat, double lng) {
        return h3.latLngToCellAddress(lat, lng, resolution);
    }

    public List<String> neighbours(String cell, int rings) {
        return h3.gridDisk(cell, rings);
    }

    public double distanceMetres(double lat1, double lng1, double lat2, double lng2) {
        return h3.greatCircleDistance(new LatLng(lat1, lng1), new LatLng(lat2, lng2), LengthUnit.m);
    }
}