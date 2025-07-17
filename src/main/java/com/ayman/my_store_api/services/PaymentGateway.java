package com.ayman.my_store_api.services;

import com.ayman.my_store_api.entities.Order;

import java.util.Optional;

public interface PaymentGateway
{
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest (WebhookRequest request);
}
