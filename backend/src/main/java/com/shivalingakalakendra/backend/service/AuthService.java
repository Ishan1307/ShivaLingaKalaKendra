package com.shivalingakalakendra.backend.service;

import com.shivalingakalakendra.backend.dto.LoginRequest;
import com.shivalingakalakendra.backend.dto.LoginResponse;
import com.shivalingakalakendra.backend.entity.User;
import com.shivalingakalakendra.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .filter(foundUser -> Boolean.TRUE.equals(foundUser.getIsActive()))
                .filter(foundUser -> passwordEncoder.matches(request.password(), foundUser.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password"
                ));

        return new LoginResponse(
                "Login successful"
        );
    }
}
