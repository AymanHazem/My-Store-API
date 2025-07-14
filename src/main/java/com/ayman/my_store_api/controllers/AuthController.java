package com.ayman.my_store_api.controllers;
import com.ayman.my_store_api.dtos.JwtResponse;
import com.ayman.my_store_api.dtos.LogInRequest;
import com.ayman.my_store_api.dtos.UserDto;
import com.ayman.my_store_api.mappers.UserMapper;
import com.ayman.my_store_api.repositories.UserRepository;
import com.ayman.my_store_api.services.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController
{
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> logIn (@Valid @RequestBody LogInRequest request)
    {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        var token =  jwtService.generateToken(request.getEmail());
        return ResponseEntity.ok(new JwtResponse(token));
    }
    @GetMapping("/me")
    public ResponseEntity<UserDto> me ()
    {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var email = (String)authentication.getPrincipal();
        var user = userRepository.findByEmail(email).orElse(null);
        if (user==null)
            return ResponseEntity.notFound().build();
        var userDto=userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/validate")
    public boolean validate(@RequestHeader("Authorization") String authHeadder)
    {
        System.out.println("validate called");
        var token=authHeadder.replace("Bearer ","");
        return jwtService.validateToken(token);
    }








    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> badCredentialsHandler ()
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
