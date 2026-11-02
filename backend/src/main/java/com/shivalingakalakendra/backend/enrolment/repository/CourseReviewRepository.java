package com.shivalingakalakendra.backend.repository;

import com.shivalingakalakendra.backend.entity.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CourseReviewRepository extends JpaRepository<CourseReview, UUID> {
    List<CourseReview> findByCourseid_IdAndIsActiveTrueOrderByCreateTimeStampDesc(UUID courseId);
}