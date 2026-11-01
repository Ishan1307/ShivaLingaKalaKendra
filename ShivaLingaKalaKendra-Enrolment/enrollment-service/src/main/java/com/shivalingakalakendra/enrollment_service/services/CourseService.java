package com.shivalingakalakendra.enrollment_service.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shivalingakalakendra.enrollment_service.dto.CourseDetailsResponse;
import com.shivalingakalakendra.enrollment_service.dto.CourseResponse;
import com.shivalingakalakendra.enrollment_service.dto.CourseReviewResponse;
import com.shivalingakalakendra.enrollment_service.dto.CreateCourseRequest;
import com.shivalingakalakendra.enrollment_service.entity.Courses;
import com.shivalingakalakendra.enrollment_service.repository.CourseRepository;
import com.shivalingakalakendra.enrollment_service.repository.CourseReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Optional<CourseResponse> getCourseById(String id) {
        return courseRepository.findById(id)
                .map(this::toResponse);
    }

    public Optional<CourseDetailsResponse> getCourseDetails(String id) {
        return courseRepository.findById(id).map(course -> {

            var reviews = courseReviewRepository
                    .findByCourseidAndIsActiveTrueOrderByCreateTimeStampDesc(id)
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

    public CourseResponse addNewCourse(CreateCourseRequest request) {
        Courses newCourse =new Courses();
        newCourse.setDescription(request.description());
        newCourse.setName(request.name());
        newCourse.setIsActive(request.isactive());
        newCourse.setCreateTimeStamp(LocalDateTime.now());
        newCourse.setDuration(request.duration());
        newCourse.setFee(request.fee());
        newCourse.setLevel(request.level());
        newCourse.setClassesPerMonth(request.classesPerMonth());
        newCourse.setSyllabusTheory(request.syllabusTheory());
        newCourse.setSyllabusPractical(request.syllabusPractical());
        newCourse.setLearningOutcomes(request.learningOutcomes());
        Courses savedCourse = courseRepository.save(newCourse);
        return toResponse(savedCourse);
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
                course.getSyllabusTheory(),
                course.getSyllabusPractical(),
                course.getLearningOutcomes()
        );
    }

    private Object toJsonValue(JsonNode value) {
        return value == null ? null : JSON_NODE_MAPPER.convertValue(value, Object.class);
    }
}
