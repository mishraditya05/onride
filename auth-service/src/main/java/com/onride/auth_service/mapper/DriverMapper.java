package com.onride.auth_service.mapper;

import com.onride.auth_service.dto.DriverResponseDto;
import com.onride.auth_service.dto.UpdateDriverRequestDto;
import com.onride.auth_service.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    DriverResponseDto toDriverResponseDto(Driver driver);

    void updateFromDto(UpdateDriverRequestDto dto, @MappingTarget Driver driver);
}