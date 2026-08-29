package com.onride.ride_service.mapper;

import com.onride.events.RideRequestedEvent;
import com.onride.ride_service.entity.Ride;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RideEventMapper {

    @Mapping(target = "rideId", source = "id")
    RideRequestedEvent toRideRequestedEvent(Ride ride);
}