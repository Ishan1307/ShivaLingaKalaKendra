package com.shivalingakalakendra.enrollment_service.entity;



import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Getter
@Setter
@Document(collection = "course_review")
public class CourseReview {


    private String id;

    private String review;


    private Float rating;


    private Boolean isActive;


    private LocalDateTime createTimeStamp;


    private String createdBy;

    private String courseid;

    protected CourseReview() {
    }

    public CourseReview(
            String id,
            String review,
            Float rating,
            Boolean isActive,
            LocalDateTime createTimeStamp,
            String courseid) {
        this.id = id;
        this.review = review;
        this.rating = rating;
        this.isActive = isActive;
        this.createTimeStamp = createTimeStamp;
        this.courseid = courseid;
    }
}
