package com.shivalingakalakendra.enrollment_service.dto;

import java.time.LocalDate;
import java.util.UUID;

public record EnrolmentResponse(
        String id,
        String status,
        String firstName,
        String lastName,
        String email,
        String guardianEmail,
        String phone,
        LocalDate dateOfBirth,
        String courseId,
        String courseName,
        String userId,
        String batchId,
        String batchName,
        String batchSchedule,
        LocalDate startDate,
        Double fee,
        String paymentStatus,
        String transactionReference
) {
}
