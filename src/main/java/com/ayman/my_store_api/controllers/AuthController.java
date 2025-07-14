package com.ayman.my_store_api.controllers;
import com.ayman.my_store_api.dtos.JwtResponse;
import com.ayman.my_store_api.dtos.LogInRequest;
import com.ayman.my_store_api.services.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController
{
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> logIn (@Valid @RequestBody LogInRequest request)
    {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        var token =  jwtService.generateToken(request.getEmail());
        return ResponseEntity.ok(new JwtResponse(token));
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
