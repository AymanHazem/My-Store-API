package com.ayman.my_store_api.dtos;

import lombok.Data;

@Data
public class UpdateUserRequest
{
    private String name;
    private String email;
}
