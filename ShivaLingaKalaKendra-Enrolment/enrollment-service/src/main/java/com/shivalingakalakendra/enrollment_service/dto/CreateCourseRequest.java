package com.shivalingakalakendra.enrollment_service.dto;

import jakarta.validation.constraints.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

public record CreateCourseRequest (
    @NotBlank @Size(max = 100) String name,
    @NotBlank @Size(max = 100) String description,
    @NotBlank Boolean isactive,
    @NotNull LocalDate createTimeStamp,
    @NotBlank @Pattern(regexp = "^[+]?\\d[\\d\\s()-]{6,22}$") String phone,
    @NotNull @Min(1) String duration,
    @NotNull Double fee,

    @NotBlank @Size(max = 50) String level,

    @NotNull @Min(4)
     Integer classesPerMonth,

    @NotNull @Min(1)
     List<String> syllabusTheory,

    @NotNull @Min(1)
     List<String> syllabusPractical,

    @NotNull @Min(1)
     List<String> learningOutcomes){

}
