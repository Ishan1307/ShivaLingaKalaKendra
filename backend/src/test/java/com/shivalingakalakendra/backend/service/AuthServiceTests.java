package com.shivalingakalakendra.backend.service;

import com.shivalingakalakendra.backend.dto.LoginRequest;
import com.shivalingakalakendra.backend.dto.LoginResponse;
import com.shivalingakalakendra.backend.entity.User;
import com.shivalingakalakendra.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTests {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthService authService = new AuthService(userRepository, passwordEncoder);

    @Test
    void loginReturnsSafeUserDetailsForValidCredentials() {
        User user = user(true);
        when(userRepository.findByEmailIgnoreCase("student@example.com"))
                .thenReturn(Optional.of(user));

        LoginResponse response = authService.login(
                new LoginRequest("student@example.com", "correct-password")
        );

        assertEquals(user.getId(), response.userId());
        assertEquals(user.getEmail(), response.email());
        assertEquals("Login successful", response.message());
    }

    @Test
    void loginRejectsInvalidPassword() {
        when(userRepository.findByEmailIgnoreCase("student@example.com"))
                .thenReturn(Optional.of(user(true)));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(new LoginRequest("student@example.com", "wrong-password"))
        );

        assertEquals(401, exception.getStatusCode().value());
    }

    @Test
    void loginRejectsInactiveUser() {
        when(userRepository.findByEmailIgnoreCase("student@example.com"))
                .thenReturn(Optional.of(user(false)));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(new LoginRequest("student@example.com", "correct-password"))
        );

        assertEquals(401, exception.getStatusCode().value());
    }

    private User user(boolean active) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        User user = new User(
                "student@example.com",
                "Test",
                "Student",
                passwordEncoder.encode("correct-password"),
                active,
                now,
                now
        );
        user.setId(UUID.randomUUID());
        return user;
    }
}
