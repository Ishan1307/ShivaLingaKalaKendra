package com.shivalingakalakendra.identity_service.controllers;

import com.shivalingakalakendra.identity_service.dto.LoginRequest;
import com.shivalingakalakendra.identity_service.dto.LoginResponse;
import com.shivalingakalakendra.identity_service.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${app.cors.allowed-origin:http://localhost:4200}")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
