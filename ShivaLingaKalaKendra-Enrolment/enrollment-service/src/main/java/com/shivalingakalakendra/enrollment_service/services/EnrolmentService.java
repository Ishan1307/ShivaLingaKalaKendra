package com.shivalingakalakendra.enrollment_service.services;

import com.shivalingakalakendra.enrollment_service.dto.*;
import com.shivalingakalakendra.enrollment_service.dto.Enums.EnrolmentStatuses;
import com.shivalingakalakendra.enrollment_service.entity.*;
import com.shivalingakalakendra.enrollment_service.external.IdentityServiceClient;
import com.shivalingakalakendra.enrollment_service.repository.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class EnrolmentService {
    private static final String DRAFT = "DRAFT";
    private static final String READY_FOR_PAYMENT = "READY_FOR_PAYMENT";
    private final EnrolmentRepository enrolmentRepository;
    private final EnrolmentPaymentRepository paymentRepository;
    private final CourseRepository courseRepository;
    private final BatchRepository batchRepository;
    private final IdentityServiceClient identityServiceClient;
    private final StudentBatchRepository studentBatchRepository;
    public EnrolmentService(
            EnrolmentRepository enrolmentRepository,
            EnrolmentPaymentRepository paymentRepository,
            CourseRepository courseRepository,
            BatchRepository batchRepository,
            IdentityServiceClient identityServiceClient, StudentBatchRepository studentBatchRepository) {
        this.enrolmentRepository = enrolmentRepository;
        this.paymentRepository = paymentRepository;
        this.courseRepository = courseRepository;
        this.batchRepository = batchRepository;
        this.identityServiceClient = identityServiceClient;
        this.studentBatchRepository = studentBatchRepository;
    }


    public EnrolmentResponse createStudentDetails(CreateEnrolmentRequest request) {
        validateStudentDetails(request);

        Enrolments enrolment = new Enrolments();
        enrolment.setFirstName(request.firstName().trim());
        enrolment.setLastName(request.lastName().trim());
        enrolment.setEmail(request.email().trim());
        enrolment.setGuardianEmail(
                request.guardianEmail() == null || request.guardianEmail().isBlank()
                        ? null
                        : request.guardianEmail().trim());
        enrolment.setPhone(request.phone().trim());
        enrolment.setDateOfBirth(request.dateOfBirth().toString());
        enrolment.setStatus(DRAFT);
        enrolment.setCreateTimeStamp(LocalDateTime.now());
        enrolment.setCourseid(request.courseId());
        enrolment.setBatchid(request.batchId());
        return toResponse(enrolmentRepository.save(enrolment));
    }


    public EnrolmentResponse updateStudentDetails(
            String enrolmentId,
            CreateEnrolmentRequest request) {
        validateStudentDetails(request);
        Enrolments enrolment = requireEnrolment(enrolmentId);
        if (!DRAFT.equals(enrolment.getStatus()) && !READY_FOR_PAYMENT.equals(enrolment.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This enrolment can no longer be changed");
        }

        enrolment.setFirstName(request.firstName().trim());
        enrolment.setLastName(request.lastName().trim());
        enrolment.setEmail(request.email().trim());
        enrolment.setGuardianEmail(
                request.guardianEmail() == null || request.guardianEmail().isBlank()
                        ? null
                        : request.guardianEmail().trim());
        enrolment.setPhone(request.phone().trim());
        enrolment.setDateOfBirth(request.dateOfBirth().toString());
        return toResponse(enrolmentRepository.save(enrolment));
    }


    public EnrolmentResponse updateSelection(
            String enrolmentId,
            SelectEnrolmentOptionsRequest request) {
        Enrolments enrolment = requireEnrolment(enrolmentId);
        if (!DRAFT.equals(enrolment.getStatus()) && !READY_FOR_PAYMENT.equals(enrolment.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This enrolment can no longer be changed");
        }

        Courses course = courseRepository.findById(request.courseId())
                .filter(Courses::getIsActive)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course is not available"));
        Batches batch = batchRepository.findById(request.batchId())
                .filter(Batches::getIsActive)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Batch is not available"));

        if (!batch.getCourseid().equals(course.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Batch does not belong to the selected course");
        }
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Kolkata"));
        if (request.startDate().isBefore(today) || !isScheduledClassDay(batch, request.startDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First class date is not valid for this batch");
        }

        enrolment.setCourseid(course.getId());
        enrolment.setBatchid(batch.getId());
        enrolment.setStartDate(request.startDate());
        enrolment.setStatus(READY_FOR_PAYMENT);
        return toResponse(enrolmentRepository.save(enrolment));
    }


    public EnrolmentResponse getEnrolment(String enrolmentId) {
        return toResponse(requireEnrolment(enrolmentId));
    }

    public List<EnrolmentResponse> getAllEnrolments() {
        return enrolmentRepository.findAll().stream()
                .map(this::toResponse).toList();
    }

    public List<EnrolmentResponse> getPendingEnrolments(String status) {
        return enrolmentRepository.findByStatus(status).stream()
                .map(this::toResponse).toList();
    }

    public EnrolmentResponse approveEnrolment(String enrolmentId) {
        Enrolments enrolment = requireEnrolment(enrolmentId);
        if (!String.valueOf(EnrolmentStatuses.PENDING).equals(enrolment.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This enrolment is not pending for approval");
        }
        CreateStudentUserRequest request =
                new CreateStudentUserRequest(
                        enrolment.getFirstName(),
                        enrolment.getLastName(),
                        enrolment.getEmail()
                );

        CreateStudentUserResponse response = identityServiceClient.createStudent(request);
        enrolment.setUserid(response.userId());
        enrolment.setStatus(String.valueOf(EnrolmentStatuses.APPROVED));

        StudentBatches studentBatch = new StudentBatches(); // to add a new entry in student batch table

        studentBatch.setUserid(response.userId());
        studentBatch.setBatchid(enrolment.getBatchid());
        studentBatch.setEnrollmentId(enrolment.getId());
        studentBatch.setCreateTimeStamp(Timestamp.valueOf(LocalDateTime.now()));
        studentBatchRepository.save(studentBatch);

//        studentBatchRepository.save(
//                new StudentBatches(
//                        response.userId(),
//                        enrolment.getBatchid(),
//                        enrolment.getId(),
//                        Timestamp.valueOf(LocalDateTime.now())
//                )
//        );
        return toResponse(enrolmentRepository.save(enrolment));
    }

    public EnrolmentPaymentResponse submitPayment(
            String enrolmentId,
            SubmitEnrolmentPaymentRequest request) {
        Enrolments enrolment = enrolmentRepository.findById(enrolmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrolment not found"));
        if (!READY_FOR_PAYMENT.equals(enrolment.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Enrolment is not ready to submit payment details");
        }
        String courseId = enrolment.getCourseid();
        if (courseId == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The enrolment course fee is unavailable");
        }
        Courses course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "The enrolment course is unavailable"));
        if (course.getFee() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The enrolment course fee is unavailable");
        }

        String reference = request.transactionReference().trim().toUpperCase(Locale.ROOT);
        if (paymentRepository.existsByTransactionReference(reference)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This transaction reference has already been submitted");
        }

        BigDecimal amount = BigDecimal.valueOf(course.getFee());
        if (amount.signum() <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The enrolment course fee is invalid");
        }

        EnrolmentPayment payment = new EnrolmentPayment();
        payment.setEnrolmentId(enrolment.getId());
        payment.setAmount(amount);
        payment.setCurrency("INR");
        payment.setMethod("UPI");
        payment.setTransactionReference(reference);
        payment.setStatus("SUBMITTED");
        payment.setSubmittedAt(java.time.Instant.now());

        EnrolmentPayment savedPayment;
        try {
            savedPayment = paymentRepository.save(payment);
        } catch (DuplicateKeyException exception) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This transaction reference has already been submitted",
                    exception);
        }

        enrolment.setStatus("PENDING_FOR_APPROVAL");
        enrolmentRepository.save(enrolment);
        return toPaymentResponse(savedPayment);
    }

    private Enrolments requireEnrolment(String enrolmentId) {
        return enrolmentRepository.findById(enrolmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrolment not found"));
    }

    private boolean isScheduledClassDay(Batches batch, LocalDate date) {
        String schedule = batch.getSchedule();
        if (schedule == null || schedule.isBlank()) {
            return false;
        }

        String fullDay = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String shortDay = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        Pattern dayPattern = Pattern.compile(
                "(?i)(?:^|[^a-z])(?:" + Pattern.quote(fullDay) + "|" + Pattern.quote(shortDay) + ")s?(?=$|[^a-z])");
        return dayPattern.matcher(schedule).find();
    }

    private void validateStudentDetails(CreateEnrolmentRequest request) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Kolkata"));
        if (request.dateOfBirth() == null || request.dateOfBirth().isAfter(today)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date of birth must be in the past");
        }
        if (request.dateOfBirth().plusYears(18).isAfter(today)
                && (request.guardianEmail() == null || request.guardianEmail().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Guardian email is required for students under 18");
        }
    }

    private EnrolmentResponse toResponse(Enrolments enrolment) {
        Courses course = enrolment.getCourseid() == null
                ? null
                : courseRepository.findById(enrolment.getCourseid()).orElse(null);
        Batches batch = enrolment.getBatchid() == null
                ? null
                : batchRepository.findById(enrolment.getBatchid()).orElse(null);
        EnrolmentPayment latestPayment = paymentRepository
                .findTopByEnrolmentIdOrderBySubmittedAtDesc(enrolment.getId())
                .orElse(null);
        System.out.println(
                "Enrollment Id : " + enrolment.getId()
        );

        System.out.println(
                "User Id : " + enrolment.getUserid()
        );
        return new EnrolmentResponse(
                enrolment.getId(),
                enrolment.getStatus(),
                enrolment.getFirstName(),
                enrolment.getLastName(),
                enrolment.getEmail(),
                enrolment.getGuardianEmail(),
                enrolment.getPhone(),
                LocalDate.parse(enrolment.getDateOfBirth()),
                enrolment.getCourseid(),
                course == null ? null : course.getName(),
                enrolment.getUserid(),
                enrolment.getBatchid(),
                batch == null ? null : batch.getName(),
                batch == null ? null : batch.getSchedule(),
                enrolment.getStartDate(),
                course == null ? null : course.getFee(),
                latestPayment == null ? null : latestPayment.getStatus(),
                latestPayment == null ? null : latestPayment.getTransactionReference());
    }

    private EnrolmentPaymentResponse toPaymentResponse(EnrolmentPayment payment) {
        return new EnrolmentPaymentResponse(
                payment.getId(),
                payment.getEnrolmentId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getMethod(),
                payment.getTransactionReference(),
                payment.getStatus(),
                payment.getSubmittedAt());
    }
}
