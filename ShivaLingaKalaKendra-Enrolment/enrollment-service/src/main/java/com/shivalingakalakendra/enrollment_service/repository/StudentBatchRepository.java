package com.shivalingakalakendra.enrollment_service.repository;

import com.shivalingakalakendra.enrollment_service.entity.StudentBatches;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentBatchRepository extends MongoRepository<StudentBatches,String> {
}
