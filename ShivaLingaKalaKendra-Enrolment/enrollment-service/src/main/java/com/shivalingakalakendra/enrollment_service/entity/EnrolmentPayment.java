package com.shivalingakalakendra.enrollment_service.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter

@Document(collection = "enrolment_payments")
public class EnrolmentPayment {

    private String id;

    private String enrolmentId;

    private BigDecimal amount;

    private String currency;

    private String method;

    @Indexed(unique = true)
    private String transactionReference;

    private String status;

    private Instant submittedAt;

    private Instant reviewedAt;

    private String reviewedBy;

    private String reviewNote;

//    private String userId;
}
