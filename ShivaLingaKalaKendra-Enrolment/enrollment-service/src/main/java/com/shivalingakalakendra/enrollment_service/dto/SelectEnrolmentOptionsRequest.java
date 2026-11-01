package com.shivalingakalakendra.enrollment_service.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record SelectEnrolmentOptionsRequest(
        @NotNull String courseId,
        @NotNull String batchId,
        @NotNull LocalDate startDate
) {
}
