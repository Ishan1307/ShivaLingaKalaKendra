package com.shivalingakalakendra.backend.dto;

import java.sql.Timestamp;
import java.util.UUID;

public record CourseResponse(
        UUID id,
        String name,
        String description,
        Boolean isActive,
        Double fee,
        String duration,
        Timestamp createTimeStamp,
        String level,
        Integer classesPerMonth,
        Object syllabusTheory,
        Object syllabusPractical,
        Object learningOutcomes
) {

}
