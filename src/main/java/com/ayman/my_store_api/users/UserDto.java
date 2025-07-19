package com.ayman.my_store_api.users;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto
{
    private Long id;
    private String name;
    private String email;

}
