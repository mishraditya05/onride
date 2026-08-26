package com.onride.common.web.geo;

import com.uber.h3core.H3Core;
import com.uber.h3core.LengthUnit;
import com.uber.h3core.util.LatLng;

import java.util.List;

public class GeoIndex {

    private static final int RESOLUTION = 9;

    private final H3Core h3;

    public GeoIndex(H3Core h3) {
        this.h3 = h3;
    }

    public String toCell(double lat, double lng) {
        return h3.latLngToCellAddress(lat, lng, RESOLUTION);
    }

    public List<String> neighbours(String cell, int rings) {
        return h3.gridDisk(cell, rings);
    }

    public double distanceMetres(double lat1, double lng1, double lat2, double lng2) {
        return h3.greatCircleDistance(new LatLng(lat1, lng1), new LatLng(lat2, lng2), LengthUnit.m);
    }
}