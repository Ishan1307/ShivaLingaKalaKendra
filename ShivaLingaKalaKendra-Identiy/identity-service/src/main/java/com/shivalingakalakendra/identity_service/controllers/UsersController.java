package com.shivalingakalakendra.identity_service.controllers;

import com.shivalingakalakendra.identity_service.dto.*;
import com.shivalingakalakendra.identity_service.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        UserResponse createdUser = userService.createUser(request);
        return ResponseEntity.created(uriBuilder
                .path("/api/users/{id}")
                .buildAndExpand(createdUser.id())
                .toUri()).body(createdUser);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/roles")
    public ResponseEntity<AddRoleResponse> addUserRole(
            @Valid @RequestBody AddUserRoleRequest request
    ) {
        AddRoleResponse response = userService.addUserRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/createuser/student")
    public CreateStudentUserResponse createStudentUser(@Valid @RequestBody CreateStudentUserRequest request) {
        return userService.createStudentUser(request);
    }
}
