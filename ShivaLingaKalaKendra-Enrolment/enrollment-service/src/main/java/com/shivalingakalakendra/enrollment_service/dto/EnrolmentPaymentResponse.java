package com.shivalingakalakendra.enrollment_service.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record EnrolmentPaymentResponse(
        String id,
        String enrolmentId,
        BigDecimal amount,
        String currency,
        String method,
        String transactionReference,
        String status,
        Instant submittedAt
) {
}
