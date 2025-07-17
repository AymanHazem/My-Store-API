package com.ayman.my_store_api.services;

import com.ayman.my_store_api.entities.Order;

public interface PaymentGateway
{
    CheckoutSession createCheckoutSession(Order order);
}
