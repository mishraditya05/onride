package com.onride.ride_service.util;

public final class GeoUtils {

    private static final double EARTH_RADIUS_METRES = 6_371_000;

    private GeoUtils() {
    }

    public static double haversineMetres(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.pow(Math.sin(dLng / 2), 2);

        return EARTH_RADIUS_METRES * 2 * Math.asin(Math.sqrt(a));
    }
}