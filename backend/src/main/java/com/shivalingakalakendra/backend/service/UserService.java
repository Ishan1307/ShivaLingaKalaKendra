package com.shivalingakalakendra.backend.service;

import com.shivalingakalakendra.backend.dto.CreateUserRequest;
import com.shivalingakalakendra.backend.dto.UserResponse;
import com.shivalingakalakendra.backend.entity.User;
import com.shivalingakalakendra.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A user with this email already exists");
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        User user = new User(
                request.email(),
                request.firstName(),
                request.lastName(),
                passwordEncoder.encode(request.password()),
                true,
                now,
                now
        );

        return toResponse(userRepository.save(user));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<UserResponse> getUserById(UUID id) {
        return userRepository.findById(id)
                .map(this::toResponse);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getIsActive(),
                user.getCreateTimeStamp(),
                user.getModifyTimeStamp()
        );
    }
}
