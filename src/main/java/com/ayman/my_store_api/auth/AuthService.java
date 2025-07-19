package com.ayman.my_store_api.auth;

import com.ayman.my_store_api.users.User;
import com.ayman.my_store_api.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService
{
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public User getCurrentUser()
    {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long)authentication.getPrincipal();
        return userRepository.findById(userId).orElse(null);
    }

    public LoginResponse login (LogInRequest request)
    {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var accessToken =  jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return new LoginResponse(accessToken,refreshToken);
    }

    public Jwt refreshAccessToken (String refreshToken)
    {
        var jwt = jwtService.parseToken(refreshToken);
        if (jwt==null || jwt.isExpired())
            throw new BadCredentialsException("Invalid refresh token");
        var user= userRepository.findById(jwt.getUserID()).orElseThrow();
        return jwtService.generateAccessToken(user);

    }
}
