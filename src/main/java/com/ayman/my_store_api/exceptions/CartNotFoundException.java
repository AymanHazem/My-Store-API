package com.ayman.my_store_api.exceptions;

public class CartNotFoundException extends RuntimeException
{
    public CartNotFoundException()
    {
        super("Cart Not Found.");
    }
}
