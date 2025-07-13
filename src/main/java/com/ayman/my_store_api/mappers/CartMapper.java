package com.ayman.my_store_api.mappers;

import com.ayman.my_store_api.dtos.CartDto;
import com.ayman.my_store_api.dtos.CartItemDto;
import com.ayman.my_store_api.entities.Cart;
import com.ayman.my_store_api.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper (componentModel = "spring")
public interface CartMapper
{
    @Mapping(target = "totalPrice" , expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);
    @Mapping(target = "totalPrice" , expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto (CartItem cartItem);
}
