package com.shivalingakalakendra.backend.service;

import com.shivalingakalakendra.backend.dto.LoginRequest;
import com.shivalingakalakendra.backend.dto.LoginResponse;
import com.shivalingakalakendra.backend.entity.User;
import com.shivalingakalakendra.backend.repository.UserRolesRepository;
import com.shivalingakalakendra.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            UserRolesRepository userRolesRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userRolesRepository = userRolesRepository;
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

        List<String> roles = userRolesRepository.findAllByUser_Id(user.getId()).stream()
                .map(userRole -> userRole.getRole().getName())
                .toList();

        return new LoginResponse(
                "Login successful",
                new LoginResponse.UserDetails(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        roles
                )
        );
    }
}
