package com.onride.auth_service.mapper;

import com.onride.auth_service.dto.CreateVehicleRequestDto;
import com.onride.auth_service.dto.VehicleResponseDto;
import com.onride.auth_service.entity.Vehicle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    VehicleResponseDto toVehicleResponseDto(Vehicle vehicle);

    Vehicle fromDto(CreateVehicleRequestDto dto);
}