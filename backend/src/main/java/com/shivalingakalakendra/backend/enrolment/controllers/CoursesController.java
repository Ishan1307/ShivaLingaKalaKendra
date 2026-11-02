package com.shivalingakalakendra.backend.controllers;

import com.shivalingakalakendra.backend.dto.CourseDetailsResponse;
import com.shivalingakalakendra.backend.dto.CourseResponse;
import com.shivalingakalakendra.backend.service.CourseService;
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
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable UUID id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<CourseDetailsResponse> getCourseDetails(@PathVariable UUID id) {
        return courseService.getCourseDetails(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
