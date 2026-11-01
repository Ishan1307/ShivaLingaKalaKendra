package com.shivalingakalakendra.enrollment_service.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public record CourseResponse(
        String id,
        String name,
        String description,
        Boolean isActive,
        Double fee,
        String duration,
        LocalDateTime createTimeStamp,
        String level,
        Integer classesPerMonth,
        Object syllabusTheory,
        Object syllabusPractical,
        Object learningOutcomes
) {

}
