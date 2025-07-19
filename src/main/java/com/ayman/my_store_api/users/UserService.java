package com.ayman.my_store_api.users;

import com.ayman.my_store_api.common.ErrorDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@Service
@AllArgsConstructor
public class UserService
{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    Iterable<UserDto> findAllUsers(String sortBy)
    {
        if (!Set.of("name","email").contains(sortBy))
            sortBy="name";//default sort behaviour
        return userRepository
                .findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getUser(Long userId)
    {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return userMapper.toDto(user);
    }

    public UserDto registerUser(RegisterUserRequest request)
    {

        if (userRepository.existsByEmail(request.getEmail()))
            throw new DuplicateUserException();
        var user=userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserDto updateUser(Long userId,  UpdateUserRequest request)
    {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userMapper.update(request,user);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public void deleteUser ( Long userId)
    {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }

    public void changePassword ( Long userId ,  ChangePasswordRequest request)
    {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
            throw new AccessDeniedException("Password does not match");
        user.setPassword(request.getNewPassword());
        userRepository.save(user);

    }
}
