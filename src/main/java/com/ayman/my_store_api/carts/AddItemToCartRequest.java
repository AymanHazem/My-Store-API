package com.ayman.my_store_api.carts;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class AddItemToCartRequest
{
    @NotNull
    private long productId;
}
