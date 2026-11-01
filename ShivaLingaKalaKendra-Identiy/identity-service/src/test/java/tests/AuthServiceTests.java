package tests;

import com.shivalingakalakendra.identity_service.dto.LoginRequest;
import com.shivalingakalakendra.identity_service.dto.LoginResponse;
import com.shivalingakalakendra.identity_service.entity.Roles;
import com.shivalingakalakendra.identity_service.entity.User;
import com.shivalingakalakendra.identity_service.entity.UserRoles;
import com.shivalingakalakendra.identity_service.repository.UserRepository;
import com.shivalingakalakendra.identity_service.repository.UserRolesRepository;
import com.shivalingakalakendra.identity_service.services.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class AuthServiceTests {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UserRolesRepository userRolesRepository = Mockito.mock(UserRolesRepository.class);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthService authService = new AuthService(
            userRepository,
            userRolesRepository,
            passwordEncoder
    );

    @Test
    void loginReturnsSafeUserDetailsAndRolesForValidCredentials() {
        User user = user(true);
        Mockito.when(userRepository.findByEmailIgnoreCase("student@example.com"))
                .thenReturn(Optional.of(user));
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Roles studentRole = role("STUDENT", now);
        Roles adminRole = role("ADMIN", now);
        Mockito.when(userRolesRepository.findAllByUser_Id(user.getId()))
                .thenReturn(List.of(
                        new UserRoles(user, studentRole, now),
                        new UserRoles(user, adminRole, now)
                ));

        LoginResponse response = authService.login(
                new LoginRequest("student@example.com", "correct-password")
        );

        Assertions.assertEquals("Login successful", response.message());
        Assertions.assertEquals(user.getId(), response.user().id());
        Assertions.assertEquals(user.getEmail(), response.user().email());
        Assertions.assertEquals(user.getFirstName(), response.user().firstName());
        Assertions.assertEquals(user.getLastName(), response.user().lastName());
        Assertions.assertEquals(List.of("STUDENT", "ADMIN"), response.user().roles());
    }

    @Test
    void loginRejectsInvalidPassword() {
        Mockito.when(userRepository.findByEmailIgnoreCase("student@example.com"))
                .thenReturn(Optional.of(user(true)));

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> authService.login(new LoginRequest("student@example.com", "wrong-password"))
        );

        Assertions.assertEquals(401, exception.getStatusCode().value());
    }

    @Test
    void loginRejectsInactiveUser() {
        Mockito.when(userRepository.findByEmailIgnoreCase("student@example.com"))
                .thenReturn(Optional.of(user(false)));

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> authService.login(new LoginRequest("student@example.com", "correct-password"))
        );

        Assertions.assertEquals(401, exception.getStatusCode().value());
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
