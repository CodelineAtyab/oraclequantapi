package com.oraclequantapi.oraclequantapi.controller;

import com.oraclequantapi.oraclequantapi.model.ConversionHistory;
import com.oraclequantapi.oraclequantapi.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// This controller exposes CRUD endpoints for conversion history
@RestController
@RequestMapping("/history")
public class HistoryController {

    private static final Logger logger = LoggerFactory.getLogger(HistoryController.class);

    private final HistoryService historyService;

    // Inject HistoryService via constructor
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    // GET /history — returns all history records
    @GetMapping
    public ResponseEntity<List<ConversionHistory>> getAll() {
        logger.info("Fetching all history records");
        return ResponseEntity.ok(historyService.getAll());
    }

    // GET /history/{id} — returns one record by ID
    @GetMapping("/{id}")
    public ResponseEntity<ConversionHistory> getById(@PathVariable Long id) {
        logger.info("Fetching history record with id: {}", id);
        return historyService.getById(id)
                .map(ResponseEntity::ok)                      // found → 200 OK
                .orElse(ResponseEntity.notFound().build());   // not found → 404
    }

    // PUT /history/{id} — fully update a record by ID
    @PutMapping("/{id}")
    public ResponseEntity<ConversionHistory> update(
            @PathVariable Long id,
            @RequestBody ConversionHistory updated) {
        logger.info("Updating history record with id: {}", id);
        return historyService.update(id, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PATCH /history/{id} — partially update a record by ID (same logic as PUT here)
    @PatchMapping("/{id}")
    public ResponseEntity<ConversionHistory> patch(
            @PathVariable Long id,
            @RequestBody ConversionHistory updated) {
        logger.info("Patching history record with id: {}", id);
        return historyService.update(id, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /history — clears all history records
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        logger.info("Deleting all history records");
        historyService.deleteAll();
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}