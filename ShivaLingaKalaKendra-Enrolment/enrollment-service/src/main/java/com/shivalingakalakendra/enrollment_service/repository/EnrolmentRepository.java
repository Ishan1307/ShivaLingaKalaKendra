package com.shivalingakalakendra.enrollment_service.repository;

import com.shivalingakalakendra.enrollment_service.entity.Enrolments;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EnrolmentRepository extends MongoRepository<Enrolments, String> {

    public default List<Enrolments> findByStatus(String status)
    {
        return findAll().stream().filter(enrolment -> status.equals(enrolment.getStatus())).toList();
    }
}
