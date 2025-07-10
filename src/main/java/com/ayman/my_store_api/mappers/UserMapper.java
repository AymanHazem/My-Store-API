package com.ayman.my_store_api.mappers;

import com.ayman.my_store_api.dtos.ChangePasswordRequest;
import com.ayman.my_store_api.dtos.RegisterUserRequest;
import com.ayman.my_store_api.dtos.UpdateUserRequest;
import com.ayman.my_store_api.dtos.UserDto;
import com.ayman.my_store_api.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper
{
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request,@MappingTarget User user);
}
