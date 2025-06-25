package com.example.bookhub.mapper;

import com.example.bookhub.config.MapperConfig;
import com.example.bookhub.dto.user.UserRegistrationRequestDto;
import com.example.bookhub.dto.user.UserResponseDto;
import com.example.bookhub.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto modelToResponse(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
