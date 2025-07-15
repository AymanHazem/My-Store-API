package com.ayman.my_store_api.dtos;

import lombok.Data;

@Data
public class CheckOutResponse
{
    private long orderId;

    public CheckOutResponse(long orderId) {
        this.orderId = orderId;
    }
}
