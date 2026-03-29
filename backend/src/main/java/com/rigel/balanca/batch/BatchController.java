package com.rigel.balanca.batch;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/batches")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping
    public List<BatchResponse> listBatches() {
        return batchService.listAllBatches();
    }

    @PostMapping
    public BatchResponse createBatch(@Valid @RequestBody BatchUpsertRequest request) {
        return batchService.createBatch(request);
    }

    @PatchMapping("/{id}")
    public BatchResponse updateBatch(@PathVariable UUID id, @Valid @RequestBody BatchUpsertRequest request) {
        return batchService.updateBatch(id, request);
    }
}
