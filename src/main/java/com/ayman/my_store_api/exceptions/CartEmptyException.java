package com.ayman.my_store_api.exceptions;

public class CartEmptyException extends RuntimeException
{
    public CartEmptyException()
    {
      super("Cart Is Empty.");
    }
}
