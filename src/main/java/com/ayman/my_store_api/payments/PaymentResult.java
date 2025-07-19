package com.ayman.my_store_api.payments;

import com.ayman.my_store_api.orders.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult
{
    private Long orderId;
    private PaymentStatus paymentStatus;
}
