package com.shivalingakalakendra.identity_service.dto;

import java.util.UUID;

public record CreateStudentUserResponse(
        UUID userId,
        String email,
        String generatedPassword
) {
}