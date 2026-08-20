package com.onride.auth_service.service;

import com.onride.auth_service.dto.SignupRequestDto;
import com.onride.auth_service.dto.UserResponseDto;
import com.onride.auth_service.entity.User;
import com.onride.auth_service.enums.Role;
import com.onride.auth_service.exception.EmailAlreadyExistsException;
import com.onride.auth_service.exception.ResourceNotFoundException;
import com.onride.auth_service.mapper.UserMapper;
import com.onride.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RiderService riderService;

    @Transactional
    public UserResponseDto signup(SignupRequestDto request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already registered: " + request.email());
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        // Using saveAndFlush to instantly commit the transaction to prevent null on the createdAt timestamp
        User saved = userRepository.saveAndFlush(user);

        if (saved.getRole() == Role.RIDER) {
            riderService.createInitialProfile(saved.getId());
        }

        return userMapper.toResponse(saved);
    }

    public UserResponseDto getUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return userMapper.toResponse(user);
    }
}
