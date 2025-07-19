package com.ayman.my_store_api.carts;

public class CartEmptyException extends RuntimeException
{
    public CartEmptyException()
    {
      super("Cart Is Empty.");
    }
}
