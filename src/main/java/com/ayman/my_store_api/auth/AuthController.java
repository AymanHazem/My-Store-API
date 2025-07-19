package com.ayman.my_store_api.auth;
import com.ayman.my_store_api.users.UserDto;
import com.ayman.my_store_api.users.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController
{
    private final AuthService authService;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;

    @PostMapping("/login")
    public JwtResponse logIn (@Valid @RequestBody LogInRequest request, HttpServletResponse response)
    {
        var loginResult = authService.login(request);
        var refreshToken = loginResult.getRefreshToken().toString();
        var cookie = new Cookie("refreshToken",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);
        return new JwtResponse(loginResult.getAccessToken().toString());
    }
    @GetMapping("/me")
    public ResponseEntity<UserDto> me ()
    {
        var user = authService.getCurrentUser();
        if (user==null)
            return ResponseEntity.notFound().build();
        var userDto=userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh (@CookieValue(value = "refreshToken") String refreshToken)
    {
        var accessToken = authService.refreshAccessToken(refreshToken);
        return new JwtResponse(accessToken.toString());
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> badCredentialsHandler ()
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
