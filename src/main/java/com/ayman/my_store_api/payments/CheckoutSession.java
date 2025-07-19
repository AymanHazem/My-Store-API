package com.ayman.my_store_api.payments;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutSession
{
    private String checkoutUrl;
}
