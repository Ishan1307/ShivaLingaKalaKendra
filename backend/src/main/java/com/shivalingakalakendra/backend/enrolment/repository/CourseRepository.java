package com.shivalingakalakendra.backend.repository;

import com.shivalingakalakendra.backend.entity.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseRepository  extends JpaRepository<Courses, UUID> {

}
