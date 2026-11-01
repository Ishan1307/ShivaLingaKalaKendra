package com.shivalingakalakendra.enrollment_service;

import com.shivalingakalakendra.enrollment_service.dto.*;
import com.shivalingakalakendra.enrollment_service.entity.Batches;
import com.shivalingakalakendra.enrollment_service.entity.Courses;
import com.shivalingakalakendra.enrollment_service.entity.EnrolmentPayment;
import com.shivalingakalakendra.enrollment_service.entity.Enrolments;
import com.shivalingakalakendra.enrollment_service.external.IdentityServiceClient;
import com.shivalingakalakendra.enrollment_service.repository.*;
import com.shivalingakalakendra.enrollment_service.services.EnrolmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;
import java.util.UUID;

class EnrolmentServiceTests {
    private final EnrolmentRepository enrolmentRepository = Mockito.mock(EnrolmentRepository.class);
    private final EnrolmentPaymentRepository paymentRepository = Mockito.mock(EnrolmentPaymentRepository.class);
    private final CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
    private final BatchRepository batchRepository = Mockito.mock(BatchRepository.class);
    private StudentBatchRepository studentBatchRepository = Mockito.mock(StudentBatchRepository.class);
    private IdentityServiceClient identityServiceClient = Mockito.mock(IdentityServiceClient.class);
    private final EnrolmentService enrolmentService = new EnrolmentService(
            enrolmentRepository,
            paymentRepository,
            courseRepository,
            batchRepository,
            identityServiceClient,
            studentBatchRepository);

    @Test
    void createStudentDetailsReturnsCreatedEnrolmentWithUuid() {
        Mockito.when(enrolmentRepository.save(ArgumentMatchers.any(Enrolments.class))).thenAnswer(invocation -> {
            Enrolments saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID().toString());
            return saved;
        });

        EnrolmentResponse created = enrolmentService.createStudentDetails(studentDetails());

        Assertions.assertEquals("DRAFT", created.status());
        Assertions.assertEquals("Asha", created.firstName());
        Assertions.assertEquals("+919876543210", created.phone());
        org.junit.jupiter.api.Assertions.assertNotNull(created.id());
    }

    @Test
    void getEnrolmentReturnsSavedDraftByUuid() {
        String enrolmentId = UUID.randomUUID().toString();
        Enrolments enrolment = new Enrolments();
        enrolment.setId(enrolmentId);
        enrolment.setFirstName("Asha");
        enrolment.setLastName("Rao");
        enrolment.setEmail("asha@example.com");
        enrolment.setPhone("+919876543210");
        enrolment.setDateOfBirth("2000-12-12");
        enrolment.setStatus("DRAFT");
        Mockito.when(enrolmentRepository.save(ArgumentMatchers.any(Enrolments.class))).thenAnswer(invocation -> {
            Enrolments saved = invocation.getArgument(0);
            saved.setId(enrolmentId);
            return saved;
        });
        Mockito.when(enrolmentRepository.findById(enrolmentId)).thenReturn(Optional.of(enrolment));

        EnrolmentResponse restored = enrolmentService.getEnrolment(enrolmentId);
        Assertions.assertEquals(enrolmentId, restored.id());
        Assertions.assertEquals("Asha", restored.firstName());
    }

    @Test
    void getEnrolmentReturnsNotFoundForUnknownUuid() {
        String enrolmentId = UUID.randomUUID().toString();
        Mockito.when(enrolmentRepository.findById(enrolmentId)).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> enrolmentService.getEnrolment(enrolmentId));

        Assertions.assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void updateSelectionValidatesAndSavesCourseBatchAndScheduleDate() {
        LocalDate firstWednesday = LocalDate.now()
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
        String  enrolmentId = UUID.randomUUID().toString();
        String courseId = UUID.randomUUID().toString();
        String batchId = UUID.randomUUID().toString();
        Enrolments enrolment = new Enrolments();
        enrolment.setId(enrolmentId);
        enrolment.setFirstName("Asha");
        enrolment.setLastName("Rao");
        enrolment.setEmail("asha@example.com");
        enrolment.setPhone("+919876543210");
        enrolment.setDateOfBirth("2000-12-12");
        enrolment.setStatus("DRAFT");
        Courses course = new Courses(
                courseId, "Kathak", null, true, null, null, 2500.0, null,
                null, null, null, null);
        Batches batch = new Batches(
                batchId, "Evening batch", "Monday, Wednesday, Friday", "20", "5",
                null, true, null, courseId);
        Mockito.when(enrolmentRepository.findById(enrolmentId)).thenReturn(Optional.of(enrolment));
        Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        Mockito.when(batchRepository.findById(batchId)).thenReturn(Optional.of(batch));
        Mockito.when(enrolmentRepository.save(ArgumentMatchers.any(Enrolments.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EnrolmentResponse response = enrolmentService.updateSelection(
                enrolmentId,
                new SelectEnrolmentOptionsRequest(courseId, batchId, firstWednesday));

        Assertions.assertEquals("READY_FOR_PAYMENT", response.status());
        Assertions.assertEquals(courseId, response.courseId());
        Assertions.assertEquals(batchId, response.batchId());
        Assertions.assertEquals(firstWednesday, response.startDate());
        Assertions.assertEquals(2500.0, response.fee());
    }

    @Test
    void submitPaymentStoresReferenceAndMovesEnrolmentToPendingApproval() {
        String enrolmentId = UUID.randomUUID().toString();
        Courses course = new Courses(
                "course-uuid-123", "Kathak", null, true, null, null, 2500.0, null,
                null, null, null, null);
        Enrolments enrolment = new Enrolments();
        enrolment.setId(enrolmentId);
        enrolment.setCourseid(course.getId());
        enrolment.setStatus("READY_FOR_PAYMENT");
        Mockito.when(enrolmentRepository.findById(enrolmentId)).thenReturn(Optional.of(enrolment));
        Mockito.when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        Mockito.when(paymentRepository.existsByTransactionReference("UPI12345678")).thenReturn(false);
        Mockito.when(paymentRepository.save(ArgumentMatchers.any(EnrolmentPayment.class))).thenAnswer(invocation -> {
            EnrolmentPayment payment = invocation.getArgument(0);
            payment.setId(UUID.randomUUID().toString());
            return payment;
        });

        EnrolmentPaymentResponse response = enrolmentService.submitPayment(
                enrolmentId,
                new SubmitEnrolmentPaymentRequest("upi12345678"));

        Assertions.assertEquals(enrolmentId, response.enrolmentId());
        Assertions.assertEquals(new BigDecimal("2500.0"), response.amount());
        Assertions.assertEquals("INR", response.currency());
        Assertions.assertEquals("UPI12345678", response.transactionReference());
        Assertions.assertEquals("SUBMITTED", response.status());
        Assertions.assertEquals("PENDING_FOR_APPROVAL", enrolment.getStatus());
    }

    @Test
    void submitPaymentRejectsAReferenceAlreadyUsed() {
        String enrolmentId = UUID.randomUUID().toString();
        Enrolments enrolment = new Enrolments();
        enrolment.setId(enrolmentId);
        Courses course = new Courses(
                "course-uuid-123", "Kathak", null, true, null, null, 2500.0, null,
                null, null, null, null);
        enrolment.setCourseid(course.getId());
        enrolment.setStatus("READY_FOR_PAYMENT");
        Mockito.when(enrolmentRepository.findById(enrolmentId)).thenReturn(Optional.of(enrolment));
        Mockito.when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        Mockito.when(paymentRepository.existsByTransactionReference("UPI12345678")).thenReturn(true);

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> enrolmentService.submitPayment(
                        enrolmentId,
                        new SubmitEnrolmentPaymentRequest("upi12345678")));

        Assertions.assertEquals(409, exception.getStatusCode().value());
    }

    @Test
    void submitPaymentMapsMongoDuplicateReferenceToConflict() {
        String enrolmentId = UUID.randomUUID().toString();
        Enrolments enrolment = new Enrolments();
        enrolment.setId(enrolmentId);
        Courses course = new Courses(
                "course-uuid-123", "Kathak", null, true, null, null, 2500.0, null,
                null, null, null, null);
        enrolment.setCourseid(course.getId());
        enrolment.setStatus("READY_FOR_PAYMENT");
        Mockito.when(enrolmentRepository.findById(enrolmentId)).thenReturn(Optional.of(enrolment));
        Mockito.when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        Mockito.when(paymentRepository.existsByTransactionReference("UPI12345678")).thenReturn(false);
        Mockito.when(paymentRepository.save(ArgumentMatchers.any(EnrolmentPayment.class)))
                .thenThrow(new DuplicateKeyException("duplicate transaction reference"));

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> enrolmentService.submitPayment(
                        enrolmentId,
                        new SubmitEnrolmentPaymentRequest("upi12345678")));

        Assertions.assertEquals(409, exception.getStatusCode().value());
        Mockito.verify(enrolmentRepository, Mockito.never()).save(enrolment);
    }

    private CreateEnrolmentRequest studentDetails() {
        return new CreateEnrolmentRequest(
                " Asha ",
                "Rao",
                "asha@example.com",
                LocalDate.of(2000, 12, 12),
                "+919876543210",
                null,
                "sgadhgasdhas37127",
                "sgadhgasdhas37127"
        );
    }

}
