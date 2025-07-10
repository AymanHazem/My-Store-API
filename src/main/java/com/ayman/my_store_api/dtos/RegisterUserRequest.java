package com.ayman.my_store_api.dtos;
import com.ayman.my_store_api.validation.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class RegisterUserRequest
{
    @NotBlank (message = "Name is required")
    @Size (max = 255 , message = "Name Must be less than 255 chars")
    private String name;

    @NotBlank (message = "Email is required")
    @Email (message = "Email Must be valid")
    @Lowercase (message = "Email Must be Lowercase")
    private String email;

    @NotBlank (message = "Password is required")
    @Size (min = 3 , max = 26 , message = "Password Must be between 3 and 26")
    private String password;
}
