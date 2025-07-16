package com.ayman.my_store_api.exceptions;

public class OrderNotFoundException extends RuntimeException
{
    public OrderNotFoundException() {
        super("Order Not Found.");
    }
}
