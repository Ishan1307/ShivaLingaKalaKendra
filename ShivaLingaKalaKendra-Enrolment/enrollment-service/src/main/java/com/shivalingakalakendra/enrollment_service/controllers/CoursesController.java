package com.shivalingakalakendra.enrollment_service.controllers;

import com.shivalingakalakendra.enrollment_service.dto.CourseDetailsResponse;
import com.shivalingakalakendra.enrollment_service.dto.CourseResponse;
import com.shivalingakalakendra.enrollment_service.dto.CreateCourseRequest;
import com.shivalingakalakendra.enrollment_service.entity.Courses;
import com.shivalingakalakendra.enrollment_service.services.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "${app.cors.allowed-origin:http://localhost:4200}")
public class CoursesController {

    private final CourseService courseService;
    public CoursesController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseResponse> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable String id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<CourseDetailsResponse> getCourseDetails(@PathVariable String id) {
        return courseService.getCourseDetails(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add-course")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CourseResponse> addNewCourse(@RequestBody CreateCourseRequest request) {
        CourseResponse createCourse = courseService.addNewCourse(request);
        return ResponseEntity.ok(createCourse);
    }
}
