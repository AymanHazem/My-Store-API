package com.ayman.my_store_api.carts;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest
{
    @NotNull
    @Min(value = 1)
    private Integer quantity;
}
