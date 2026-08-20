package com.onride.auth_service.mapper;

import com.onride.auth_service.dto.SignupRequestDto;
import com.onride.auth_service.dto.UserResponseDto;
import com.onride.auth_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toResponse(User user);

    @Mapping(target = "password", ignore = true)
    User toEntity(SignupRequestDto request);
}
