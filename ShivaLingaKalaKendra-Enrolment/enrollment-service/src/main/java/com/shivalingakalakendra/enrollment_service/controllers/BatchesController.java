package com.shivalingakalakendra.enrollment_service.controllers;

import com.shivalingakalakendra.enrollment_service.dto.BatchRequest;
import com.shivalingakalakendra.enrollment_service.dto.BatchResponse;
import com.shivalingakalakendra.enrollment_service.services.BatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
@CrossOrigin(origins = "${app.cors.allowed-origin:http://localhost:4200}")
public class BatchesController {

    private final BatchService batchService;

    public BatchesController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping
    public List<BatchResponse> getAllBatches() {
        return batchService.getAllBatches();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchResponse> getBatchById(@PathVariable String id) {
        return batchService.getBatchById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add-batch")
    @ResponseStatus(HttpStatus.CREATED)
    public BatchResponse createBatch(@RequestBody BatchRequest request) {
        return batchService.createBatch(request);
    }
}
