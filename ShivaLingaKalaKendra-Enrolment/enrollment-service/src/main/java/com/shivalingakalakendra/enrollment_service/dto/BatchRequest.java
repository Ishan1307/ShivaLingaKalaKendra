package com.shivalingakalakendra.enrollment_service.dto;

import java.time.LocalDateTime;

public record BatchRequest(
        String name,
        String schedule,
        String capacity,
        String strength,
        String instructor,
        Boolean isActive,
        LocalDateTime createTimeStamp,
        String courseid
) {
}
