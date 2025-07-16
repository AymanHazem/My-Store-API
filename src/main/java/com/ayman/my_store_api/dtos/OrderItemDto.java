package com.ayman.my_store_api.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto
{
    private OrderProductDto product;
    private int quantity;
    private BigDecimal totalPrice;


}
