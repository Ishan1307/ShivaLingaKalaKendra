package com.shivalingakalakendra.enrollment_service.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateEnrolmentRequest(
        @NotBlank @Size(max = 100) String firstName,
        @NotBlank @Size(max = 100) String lastName,
        @NotBlank @Email @Size(max = 320) String email,
        @NotNull @Past LocalDate dateOfBirth,
        @NotBlank @Pattern(regexp = "^[+]?\\d[\\d\\s()-]{6,22}$") String phone,
        @Email @Size(max = 320) String guardianEmail,
        @NotNull String batchId,
        @NotNull String courseId
) {
}
