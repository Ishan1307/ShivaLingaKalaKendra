package com.shivalingakalakendra.backend.service;

import com.shivalingakalakendra.backend.identity.dto.LoginRequest;
import com.shivalingakalakendra.backend.identity.dto.LoginResponse;
import com.shivalingakalakendra.backend.identity.entity.Roles;
import com.shivalingakalakendra.backend.identity.entity.User;
import com.shivalingakalakendra.backend.identity.entity.UserRoles;
import com.shivalingakalakendra.backend.identity.repository.UserRepository;
import com.shivalingakalakendra.backend.identity.repository.UserRolesRepository;
import com.shivalingakalakendra.backend.identity.services.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTests {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserRolesRepository userRolesRepository = mock(UserRolesRepository.class);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthService authService = new AuthService(
            userRepository,
            userRolesRepository,
            passwordEncoder
    );

    @Test
    void loginReturnsSafeUserDetailsAndRolesForValidCredentials() {
        User user = user(true);
        when(userRepository.findByEmailIgnoreCase("student@example.com"))
                .thenReturn(Optional.of(user));
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Roles studentRole = role("STUDENT", now);
        Roles adminRole = role("ADMIN", now);
        when(userRolesRepository.findAllByUser_Id(user.getId()))
                .thenReturn(List.of(
                        new UserRoles(user, studentRole, now),
                        new UserRoles(user, adminRole, now)
                ));

        LoginResponse response = authService.login(
                new LoginRequest("student@example.com", "correct-password")
        );

        assertEquals("Login successful", response.message());
        assertEquals(user.getId(), response.user().id());
        assertEquals(user.getEmail(), response.user().email());
        assertEquals(user.getFirstName(), response.user().firstName());
        assertEquals(user.getLastName(), response.user().lastName());
        assertEquals(List.of("STUDENT", "ADMIN"), response.user().roles());
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

    private Roles role(String name, Timestamp now) {
        return new Roles(name, name + " role", "test", "test", now, now);
    }
}
