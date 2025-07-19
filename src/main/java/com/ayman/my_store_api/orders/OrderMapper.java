package com.ayman.my_store_api.orders;


import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper
{
    OrderDto toDto(Order order);
}
