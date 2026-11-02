package com.shivalingakalakendra.backend.dto;

import java.util.List;
import java.util.UUID;

public record LoginResponse(
        String message,
        UserDetails user
) {
    public record UserDetails(
            UUID id,
            String email,
            String firstName,
            String lastName,
            List<String> roles
    ) {
    }
}
