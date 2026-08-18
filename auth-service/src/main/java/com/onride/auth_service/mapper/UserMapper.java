package com.onride.auth_service.mapper;

import com.onride.auth_service.dto.UserResponse;
import com.onride.auth_service.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);
}
