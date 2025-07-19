package com.ayman.my_store_api.payments;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentException extends RuntimeException
{
    public PaymentException(String message) {
        super(message);
    }
}
