package com.shivalingakalakendra.backend.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "batches")
public class Batches {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "schedule", nullable = true, length = 255)
    private String schedule;

    @Column(name = "capacity", nullable = false, length = 255)
    private String capacity;

    @Column(name = "strength", nullable = false, length = 255)
    private String strength;

    @Column(name = "instructor", length = 150)
    private String instructor;

    @Column(name = "isActive", nullable = false, length = 100)
    private Boolean isActive;

    @Column(name = "createTimeStamp", nullable = false, length = 100)
    private Timestamp createTimeStamp;

    @ManyToOne(optional = false)
    @JoinColumn(name = "courseid", nullable = false,
            foreignKey = @ForeignKey(name = "fk_courses_batch_courses"))
    private Courses courseid;

    protected Batches() {
    }

    public Batches(
            UUID id,
            String name,
            String schedule,
            String capacity,
            String strength,
            String instructor,
            Boolean isActive,
            Timestamp createTimeStamp,
            Courses courseid) {
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