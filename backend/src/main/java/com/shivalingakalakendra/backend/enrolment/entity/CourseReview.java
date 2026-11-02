package com.shivalingakalakendra.backend.entity;


import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "coursereview")
public class CourseReview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "review", nullable = false, length = 1000)
    private String review;

    @Column(name = "rating", nullable = false, length = 100)
    private Float rating;

    @Column(name = "isActive", nullable = false, length = 100)
    private Boolean isActive;

    @Column(name = "createTimeStamp", nullable = false, length = 100)
    private Timestamp createTimeStamp;

    @Column(name = "createdBy", nullable = false, length = 100)
    private String createdBy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "courseid", nullable = false,
            foreignKey = @ForeignKey(name = "fk_courses_review_courses"))
    private Courses courseid;

    protected CourseReview() {
    }

    public CourseReview(
            UUID id,
            String review,
            Float rating,
            Boolean isActive,
            Timestamp createTimeStamp,
            Courses courseid) {
        this.id = id;
        this.review = review;
        this.rating = rating;
        this.isActive = isActive;
        this.createTimeStamp = createTimeStamp;
        this.courseid = courseid;
    }
}
