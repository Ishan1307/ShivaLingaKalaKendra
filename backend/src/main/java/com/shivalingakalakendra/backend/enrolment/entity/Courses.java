package com.shivalingakalakendra.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "courses")
public class Courses {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = true, length = 255)
    private String description;

    @Column(name = "isActive", nullable = false, length = 100)
    private Boolean isActive;

    @Column(name = "createTimeStamp", nullable = false, length = 100)
    private Timestamp createTimeStamp;

    @Column(name = "duration", nullable = true, length = 100)
    private String duration;

    @Column(name = "Fee", nullable = false, length = 100)
    private Double fee;

    @Column(name = "level", length = 50)
    private String level;

    @Column(name = "classes_per_month")
    private Integer classesPerMonth;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "syllabus_theory", columnDefinition = "jsonb")
    private JsonNode syllabusTheory;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "syllabus_practical", columnDefinition = "jsonb")
    private JsonNode syllabusPractical;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "learning_outcomes", columnDefinition = "jsonb")
    private JsonNode learningOutcomes;

    protected Courses() {
    }

    public Courses(
            UUID id,
            String name,
            String description,
            Boolean isActive,
            Timestamp createTimeStamp,
            String duration,
            Double fee,
            String level,
            Integer classesPerMonth,
            JsonNode syllabusTheory,
            JsonNode syllabusPractical,
            JsonNode learningOutcomes) {
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