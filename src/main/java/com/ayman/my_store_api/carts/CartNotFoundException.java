package com.ayman.my_store_api.carts;

public class CartNotFoundException extends RuntimeException
{
    public CartNotFoundException()
    {
        super("Cart Not Found.");
    }
}
