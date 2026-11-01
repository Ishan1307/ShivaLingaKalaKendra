// CourseReviewResponse.java
package com.shivalingakalakendra.enrollment_service.dto;

import java.util.UUID;

public record CourseReviewResponse(
        String id,
        String studentName,
        String review,
        Float rating
) {}