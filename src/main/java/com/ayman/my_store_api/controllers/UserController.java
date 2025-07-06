package com.ayman.my_store_api.controllers;
import com.ayman.my_store_api.dtos.UserDto;
import com.ayman.my_store_api.entities.User;
import com.ayman.my_store_api.mappers.UserMapper;
import com.ayman.my_store_api.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("users")
public class UserController
{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @GetMapping
    Iterable<UserDto> findAllUsers(@RequestParam(required = false,defaultValue = "",name = "sort") String sort)
    {
        if (!Set.of("name","email").contains(sort))
            sort="name";
        return userRepository.findAll(Sort.by(sort))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id)
    {
        var user = userRepository.findUserById(id);
        if (user==null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
