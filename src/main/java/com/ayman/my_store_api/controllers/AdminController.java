package com.ayman.my_store_api.controllers;

import com.ayman.my_store_api.mappers.UserMapper;
import com.ayman.my_store_api.repositories.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("admin")
@Tag(name = "Admin")
public class AdminController
{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/hello")
    public String hello ()
    {
        return "HI ADMIN !";
    }
}
