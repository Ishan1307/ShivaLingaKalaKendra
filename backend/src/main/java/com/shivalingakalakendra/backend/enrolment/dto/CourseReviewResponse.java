// CourseReviewResponse.java
package com.shivalingakalakendra.backend.dto;

import java.util.UUID;

public record CourseReviewResponse(
        UUID id,
        String studentName,
        String review,
        Float rating
) {}