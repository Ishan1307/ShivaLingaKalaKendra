package com.shivalingakalakendra.enrollment_service.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public record BatchResponse(
        String id,
        String name,
        String schedule,
        String capacity,
        String strength,
        String instructor,
        Boolean isActive,
        LocalDateTime createTimeStamp
) {
}
