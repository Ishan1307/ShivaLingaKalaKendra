package com.shivalingakalakendra.enrollment_service.controllers;

import com.shivalingakalakendra.enrollment_service.dto.*;
import com.shivalingakalakendra.enrollment_service.dto.Enums.EnrolmentStatuses;
import com.shivalingakalakendra.enrollment_service.services.EnrolmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/enrolments")
@CrossOrigin(origins = "${app.cors.allowed-origin:http://localhost:4200}")
public class EnrolmentsController {
    private final EnrolmentService enrolmentService;

    public EnrolmentsController(EnrolmentService enrolmentService) {
        this.enrolmentService = enrolmentService;
    }

    @PostMapping("/student-details")
    @ResponseStatus(HttpStatus.CREATED)
    public EnrolmentResponse createStudentDetails(@Valid @RequestBody CreateEnrolmentRequest request) {
        return enrolmentService.createStudentDetails(request);
    }

    @PutMapping("/{id}/student-details")
    public EnrolmentResponse updateStudentDetails(
            @PathVariable String id,
            @Valid @RequestBody CreateEnrolmentRequest request) {
        return enrolmentService.updateStudentDetails(id, request);
    }

    @PutMapping("/{id}/selection")
    public EnrolmentResponse updateSelection(
            @PathVariable String id,
            @Valid @RequestBody SelectEnrolmentOptionsRequest request) {
        return enrolmentService.updateSelection(id, request);
    }

    @GetMapping("/{id}/review")
    public EnrolmentResponse getEnrolment(@PathVariable String id) {
        return enrolmentService.getEnrolment(id);
    }

    @PostMapping("/{id}/payment-submission")
    @ResponseStatus(HttpStatus.CREATED)
    public EnrolmentPaymentResponse submitPayment(
            @PathVariable String id,
            @Valid @RequestBody SubmitEnrolmentPaymentRequest request) {
        return enrolmentService.submitPayment(id, request);
    }

    @GetMapping("/all-enrolments")
    public List<EnrolmentResponse> getAllEnrolments()
    {
        return enrolmentService.getAllEnrolments();
    }

    @GetMapping("/get-pending-enrolments")
    public List<EnrolmentResponse> getPendingEnrolments()
    {
        return enrolmentService.getPendingEnrolments(String.valueOf(EnrolmentStatuses.PENDING));
    }

    @PostMapping("/{id}/approve")
    public EnrolmentResponse approveEnrolment(@PathVariable String id) {
        return enrolmentService.approveEnrolment(id);
    }
}
