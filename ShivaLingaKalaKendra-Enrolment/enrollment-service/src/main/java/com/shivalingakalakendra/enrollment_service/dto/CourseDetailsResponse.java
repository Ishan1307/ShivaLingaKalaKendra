// CourseDetailsResponse.java
package com.shivalingakalakendra.enrollment_service.dto;

import java.util.List;

public record CourseDetailsResponse(
        CourseResponse course,
        List<CourseReviewResponse> reviews
) {}