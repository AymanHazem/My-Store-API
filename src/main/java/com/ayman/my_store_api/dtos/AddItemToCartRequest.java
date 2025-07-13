package com.ayman.my_store_api.dtos;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class AddItemToCartRequest
{
    @NotNull
    private long productId;
}
