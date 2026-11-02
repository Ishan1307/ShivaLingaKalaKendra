// CourseDetailsResponse.java
package com.shivalingakalakendra.backend.dto;

import java.util.List;

public record CourseDetailsResponse(
        CourseResponse course,
        List<CourseReviewResponse> reviews
) {}