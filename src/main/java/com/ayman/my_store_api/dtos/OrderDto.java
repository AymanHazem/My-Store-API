package com.ayman.my_store_api.dtos;

import com.ayman.my_store_api.entities.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto
{
    private long id;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemDto>items;
    private BigDecimal totalPrice;


}
