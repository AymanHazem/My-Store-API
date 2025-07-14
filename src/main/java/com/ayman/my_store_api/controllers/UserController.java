package com.ayman.my_store_api.controllers;
import com.ayman.my_store_api.dtos.ChangePasswordRequest;
import com.ayman.my_store_api.dtos.RegisterUserRequest;
import com.ayman.my_store_api.dtos.UpdateUserRequest;
import com.ayman.my_store_api.dtos.UserDto;
import com.ayman.my_store_api.mappers.UserMapper;
import com.ayman.my_store_api.repositories.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@AllArgsConstructor
@RestController
@RequestMapping("users")
@Tag(name = "Users")
public class UserController
{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
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
    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest request, UriComponentsBuilder uriBuilder)
    {

        if (userRepository.existsByEmail(request.getEmail()))
            return ResponseEntity.badRequest().body(Map.of("email","Email is Taken"));
        var user=userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        var userDto=userMapper.toDto(user);
        var uri =uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") Long id, @RequestBody UpdateUserRequest request)
    {
        var user=userRepository.findById(id).orElse(null);
        if (null==user)
            return ResponseEntity.notFound().build();
        userMapper.update(request,user);
        userRepository.save(user);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser (@PathVariable(name = "id") Long id)
    {
        var user=userRepository.findById(id).orElse(null);
        if (null==user)
            return ResponseEntity.notFound().build();
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword (@PathVariable(name = "id") Long id , @RequestBody ChangePasswordRequest request)
    {
        var user=userRepository.findById(id).orElse(null);
        if (null==user)
            return ResponseEntity.notFound().build();
        if (!request.getOldPassword().equals(user.getPassword()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }
}
