package com.onride.ride_service.mapper;

import com.onride.ride_service.dto.BookRideResponseDto;
import com.onride.ride_service.entity.Ride;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RideMapper {

    @Mapping(target = "rideId", source = "id")
    BookRideResponseDto toBookRideResponseDto(Ride ride);
}