package com.shivalingakalakendra.identity_service.services;

import com.shivalingakalakendra.identity_service.dto.*;
import com.shivalingakalakendra.identity_service.entity.Roles;
import com.shivalingakalakendra.identity_service.entity.User;
import com.shivalingakalakendra.identity_service.entity.UserRoles;
import com.shivalingakalakendra.identity_service.repository.RoleRepository;
import com.shivalingakalakendra.identity_service.repository.UserRepository;
import com.shivalingakalakendra.identity_service.repository.UserRolesRepository;
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
    private final UserRolesRepository userRolesRepository;
    private final RoleRepository rolesRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserRolesRepository userRolesRepository, RoleRepository rolesRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRolesRepository = userRolesRepository;
        this.rolesRepository = rolesRepository;
    }

    public AddRoleResponse addUserRole(AddUserRoleRequest request) {
        User user= userRepository.findById(request.userId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Roles roles=rolesRepository.findById(request.roleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        UserRoles userRole = new UserRoles();
        userRole.setUser(user);
        userRole.setRole(roles);
        userRole.setCreateTimeStamp(new Timestamp(System.currentTimeMillis()));
        return toUserRoleResponse(userRolesRepository.save(userRole));
    }

    private AddRoleResponse toUserRoleResponse(UserRoles save) {
        return new AddRoleResponse(save.getId());
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
    private String GeneratePassword() {

        return UUID.randomUUID()
                .toString()
                .substring(0, 8);
    }
    public CreateStudentUserResponse createStudentUser(CreateStudentUserRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A user with this email already exists");
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        User user = new User(
                request.email(),
                request.firstName(),
                request.lastName(),
                passwordEncoder.encode(GeneratePassword()),
                true,
                now,
                now
        );

        User savedUser = userRepository.save(user);

        Roles studentRole = rolesRepository.findByName("STUDENT");

        UserRoles userRole = new UserRoles();
        userRole.setUser(savedUser);
        userRole.setRole(studentRole);
        userRole.setCreateTimeStamp(now);
        userRolesRepository.save(userRole);

        return new CreateStudentUserResponse(savedUser.getId(), savedUser.getEmail(), savedUser.getPassword());
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
