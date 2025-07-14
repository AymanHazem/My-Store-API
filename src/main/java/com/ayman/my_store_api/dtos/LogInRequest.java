package com.ayman.my_store_api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogInRequest
{
    @NotBlank (message = "Email is Must")
    @Email
    private String email;
    @NotBlank (message = "Password is Must")
    private String password;
}
