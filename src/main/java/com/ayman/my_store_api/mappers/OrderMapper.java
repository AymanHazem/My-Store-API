package com.ayman.my_store_api.mappers;


import com.ayman.my_store_api.dtos.OrderDto;
import com.ayman.my_store_api.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper
{
    OrderDto toDto(Order order);
}
