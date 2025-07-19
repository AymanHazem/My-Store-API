package com.ayman.my_store_api.payments;

import com.ayman.my_store_api.orders.Order;

import java.util.Optional;

public interface PaymentGateway
{
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest (WebhookRequest request);
}
