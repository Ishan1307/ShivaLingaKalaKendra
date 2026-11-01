package com.shivalingakalakendra.enrollment_service.repository;

import com.shivalingakalakendra.enrollment_service.entity.EnrolmentPayment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EnrolmentPaymentRepository extends MongoRepository<EnrolmentPayment, String> {
    boolean existsByTransactionReference(String transactionReference);

    Optional<EnrolmentPayment> findTopByEnrolmentIdOrderBySubmittedAtDesc(String enrolmentId);
}
