package com.ayman.my_store_api.dtos;

import lombok.Data;

@Data
public class CheckOutResponse
{
    private long orderId;
    private String checkoutUrl;
    public CheckOutResponse(Long orderId , String checkoutUrl) {
        this.orderId = orderId;
        this.checkoutUrl= checkoutUrl;
    }
}
