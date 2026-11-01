package com.shivalingakalakendra.enrollment_service.services;

import com.shivalingakalakendra.enrollment_service.dto.BatchRequest;
import com.shivalingakalakendra.enrollment_service.dto.BatchResponse;
import com.shivalingakalakendra.enrollment_service.entity.Batches;
import com.shivalingakalakendra.enrollment_service.repository.BatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BatchService {

    private final BatchRepository batchRepository;

    public BatchService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    public List<BatchResponse> getAllBatches() {
        return batchRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<BatchResponse> getBatchById(String id) {
        return batchRepository.findById(id)
                .map(this::toResponse);
    }

    public BatchResponse createBatch(BatchRequest request) {
        Batches batch = new Batches();
        batch.setName(request.name());
        batch.setSchedule(request.schedule());
        batch.setCapacity(request.capacity());
        batch.setStrength(request.strength());
        batch.setInstructor(request.instructor());
        batch.setIsActive(request.isActive());
        batch.setCourseid(request.courseid());
        return toResponse(batchRepository.save(batch));
    }

    private BatchResponse toResponse(Batches batch) {
        return new BatchResponse(
                batch.getId(),
                batch.getName(),
                batch.getSchedule(),
                batch.getCapacity(),
                batch.getStrength(),
                batch.getInstructor(),
                batch.getIsActive(),
                batch.getCreateTimeStamp()
        );
    }
}
