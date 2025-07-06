package com.ayman.my_store_api.mappers;

import com.ayman.my_store_api.dtos.UserDto;
import com.ayman.my_store_api.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper
{
    UserDto toDto(User user);
}
