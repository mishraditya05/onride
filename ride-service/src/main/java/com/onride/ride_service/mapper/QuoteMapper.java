package com.onride.ride_service.mapper;

import com.onride.ride_service.dto.QuoteResponseDto;
import com.onride.ride_service.dto.VehicleQuoteDto;
import com.onride.ride_service.enums.VehicleType;
import com.onride.ride_service.redis.Quote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface QuoteMapper {

    @Mapping(target = "vehicle", source = "vehicle")
    QuoteResponseDto toQuoteResponseDto(Quote quote, Map<VehicleType, VehicleQuoteDto> vehicle);
}