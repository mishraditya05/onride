package com.onride.auth_service.mapper;

import com.onride.auth_service.dto.RiderResponseDto;
import com.onride.auth_service.dto.UpdateRiderRequestDto;
import com.onride.auth_service.entity.Rider;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RiderMapper {

    RiderResponseDto toRiderResponseDto(Rider rider);

    void updateFromDto(UpdateRiderRequestDto dto, @MappingTarget Rider rider);
}