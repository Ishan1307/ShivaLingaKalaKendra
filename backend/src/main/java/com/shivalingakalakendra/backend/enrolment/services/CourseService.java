package com.shivalingakalakendra.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shivalingakalakendra.backend.dto.CourseDetailsResponse;
import com.shivalingakalakendra.backend.dto.CourseResponse;
import com.shivalingakalakendra.backend.dto.CourseReviewResponse;
import com.shivalingakalakendra.backend.entity.Courses;
import com.shivalingakalakendra.backend.repository.CourseRepository;
import com.shivalingakalakendra.backend.repository.CourseReviewRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseService {
    private static final ObjectMapper JSON_NODE_MAPPER = new ObjectMapper();

    private final CourseRepository courseRepository;

    private final CourseReviewRepository courseReviewRepository;

    public CourseService(CourseRepository courseRepository, CourseReviewRepository courseReviewRepository) {
        this.courseRepository = courseRepository;
        this.courseReviewRepository = courseReviewRepository;
    }

    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<CourseResponse> getCourseById(UUID id) {
        return courseRepository.findById(id)
                .map(this::toResponse);
    }

    public Optional<CourseDetailsResponse> getCourseDetails(UUID id) {
        return courseRepository.findById(id).map(course -> {

            var reviews = courseReviewRepository
                    .findByCourseid_IdAndIsActiveTrueOrderByCreateTimeStampDesc(id)
                    .stream()
                    .filter(review -> review.getRating() != null
                            && review.getRating() >= 0
                            && review.getRating() <= 5)
                    .map(review -> new CourseReviewResponse(
                            review.getId(),
                            review.getCreatedBy(),
                            review.getReview(),
                            review.getRating()))
                    .toList();

            return new CourseDetailsResponse(toResponse(course), reviews);
        });
    }

    private CourseResponse toResponse(Courses course) {
        return new CourseResponse(
                course.getId(),
                course.getName(),
                course.getDescription(),
                course.getIsActive(),
                course.getFee(),
                course.getDuration(),
                course.getCreateTimeStamp(),
                course.getLevel(),
                course.getClassesPerMonth(),
                toJsonValue(course.getSyllabusTheory()),
                toJsonValue(course.getSyllabusPractical()),
                toJsonValue(course.getLearningOutcomes())
        );
    }

    private Object toJsonValue(JsonNode value) {
        return value == null ? null : JSON_NODE_MAPPER.convertValue(value, Object.class);
    }
}
