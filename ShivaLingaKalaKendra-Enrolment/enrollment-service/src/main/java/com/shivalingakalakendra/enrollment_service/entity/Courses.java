package com.shivalingakalakendra.enrollment_service.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "courses")
public class Courses {

    @Id
    private String id;

    @Field(name = "name")
    private String name;

    @Field(name = "description")
    private String description;

    @Field(name = "isActive")
    private Boolean isActive;

    @Field(name = "createTimeStamp")
    private LocalDateTime createTimeStamp;

    @Field(name = "duration")
    private String duration;

    @Field(name = "Fee")
    private Double fee;

    @Field(name = "level")
    private String level;

    @Field(name = "classes_per_month")
    private Integer classesPerMonth;

    @Field(name = "syllabus_theory")
    private List<String> syllabusTheory;

    @Field(name = "syllabus_practical")
    private List<String> syllabusPractical;

    @Field(name = "learning_outcomes")
    private List<String> learningOutcomes;

    public Courses() {
    }

    public Courses(
            String id,
            String name,
            String description,
            Boolean isActive,
            LocalDateTime createTimeStamp,
            String duration,
            Double fee,
            String level,
            Integer classesPerMonth,
            List<String> syllabusTheory,
            List<String> syllabusPractical,
            List<String> learningOutcomes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.createTimeStamp = createTimeStamp;
        this.duration = duration;
        this.fee = fee;
        this.level = level;
        this.classesPerMonth = classesPerMonth;
        this.syllabusTheory = syllabusTheory;
        this.syllabusPractical = syllabusPractical;
        this.learningOutcomes = learningOutcomes;
    }

}