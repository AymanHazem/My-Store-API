package com.ayman.my_store_api.users;
import com.ayman.my_store_api.common.ErrorDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
    private final UserService userService;

    @GetMapping
    Iterable<UserDto> findAllUsers(@RequestParam(required = false,defaultValue = "",name = "sort") String sortBy)
    {
        return userService.findAllUsers(sortBy);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id)
    {
        return userService.getUser(id);
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest request, UriComponentsBuilder uriBuilder)
    {
        var userDto=userService.registerUser(request);
        var uri =uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable(name = "id") Long id, @RequestBody UpdateUserRequest request)
    {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id)
    {
        userService.deleteUser(id);
    }

    @PostMapping("/{id}/change-password")
    public void changePassword (@PathVariable(name = "id") Long id , @RequestBody ChangePasswordRequest request)
    {
        userService.changePassword(id, request);
    }


    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateUser()
    {
        return ResponseEntity.badRequest().body(Map.of("email", "Email is already registered."));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFound()
    {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied()
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
