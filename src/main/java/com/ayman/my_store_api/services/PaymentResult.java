package com.ayman.my_store_api.services;

import com.ayman.my_store_api.entities.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult
{
    private Long orderId;
    private PaymentStatus paymentStatus;
}
