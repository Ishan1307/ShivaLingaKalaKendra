package com.shivalingakalakendra.enrollment_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SubmitEnrolmentPaymentRequest(
        @NotBlank
        @Size(min = 6, max = 100)
        @Pattern(regexp = "^[A-Za-z0-9-]+$")
        String transactionReference
) {
}
