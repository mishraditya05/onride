package com.onride.auth_service.service;

import com.onride.auth_service.dto.SignupRequest;
import com.onride.auth_service.dto.UserResponse;
import com.onride.auth_service.entity.User;
import com.onride.auth_service.exception.EmailAlreadyExistsException;
import com.onride.auth_service.exception.ResourceNotFoundException;
import com.onride.auth_service.mapper.UserMapper;
import com.onride.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already registered: " + request.email());
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    public UserResponse getUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return userMapper.toResponse(user);
    }
}
