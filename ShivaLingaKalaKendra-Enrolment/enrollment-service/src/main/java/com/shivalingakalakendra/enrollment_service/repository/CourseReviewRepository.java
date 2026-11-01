package com.shivalingakalakendra.enrollment_service.repository;

import com.shivalingakalakendra.enrollment_service.entity.CourseReview;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
public interface CourseReviewRepository extends MongoRepository<CourseReview, String> {
    List<CourseReview> findByCourseidAndIsActiveTrueOrderByCreateTimeStampDesc(String courseId);
}