package com.onride.auth_service.service;

import com.onride.auth_service.dto.SignupRequest;
import com.onride.auth_service.entity.User;
import com.onride.auth_service.exception.EmailAlreadyExistsException;
import com.onride.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public User signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already registered: " + request.email());
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRole(request.role());

        return userRepository.save(user);
    }
}
