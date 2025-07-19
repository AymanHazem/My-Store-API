package com.ayman.my_store_api.payments;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;
@Data
public class CheckOutRequest
{
    @NotNull (message = "Cart ID is Must")
    private UUID cartId;
}
