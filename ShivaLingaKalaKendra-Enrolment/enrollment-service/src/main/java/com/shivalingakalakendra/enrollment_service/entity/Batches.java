package com.shivalingakalakendra.enrollment_service.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Getter
@Setter

@Document(collection = "batches")
public class Batches {


    private String id;


    private String name;


    private String schedule;


    private String capacity;


    private String strength;


    private String instructor;


    private Boolean isActive;


    private LocalDateTime createTimeStamp;

    private String courseid;

    public Batches() {
    }

    public Batches(
            String id,
            String name,
            String schedule,
            String capacity,
            String strength,
            String instructor,
            Boolean isActive,
            LocalDateTime createTimeStamp,
            String courseid) {
        this.id = id;
        this.name = name;
        this.schedule = schedule;
        this.capacity = capacity;
        this.strength = strength;
        this.instructor = instructor;
        this.isActive = isActive;
        this.createTimeStamp = createTimeStamp;
        this.courseid = courseid;
    }

}