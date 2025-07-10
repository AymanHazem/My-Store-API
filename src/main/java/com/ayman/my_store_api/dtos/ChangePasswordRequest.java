package com.ayman.my_store_api.dtos;

import lombok.Data;

@Data
public class ChangePasswordRequest
{
    private String oldPassword;
    private String newPassword;
}
