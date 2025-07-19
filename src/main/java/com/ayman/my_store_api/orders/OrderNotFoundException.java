package com.ayman.my_store_api.orders;

public class OrderNotFoundException extends RuntimeException
{
    public OrderNotFoundException() {
        super("Order Not Found.");
    }
}
