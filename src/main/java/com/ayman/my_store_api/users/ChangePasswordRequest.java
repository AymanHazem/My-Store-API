package com.ayman.my_store_api.users;

import lombok.Data;

@Data
public class ChangePasswordRequest
{
    private String oldPassword;
    private String newPassword;
}
