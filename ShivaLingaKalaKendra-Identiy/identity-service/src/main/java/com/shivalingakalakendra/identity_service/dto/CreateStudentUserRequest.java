package com.shivalingakalakendra.identity_service.dto;

public record CreateStudentUserRequest(
        String firstName,
        String lastName,
        String email,
        String phone
) {
}
