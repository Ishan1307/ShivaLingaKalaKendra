package com.shivalingakalakendra.enrollment_service.repository;

import com.shivalingakalakendra.enrollment_service.entity.Courses;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseRepository  extends MongoRepository<Courses, String> {

}
