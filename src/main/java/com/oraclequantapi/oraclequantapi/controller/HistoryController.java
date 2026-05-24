package com.oraclequantapi.oraclequantapi.controller;

import com.oraclequantapi.oraclequantapi.model.HistoryRecord;
import com.oraclequantapi.oraclequantapi.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {
    private static final Logger log = LoggerFactory.getLogger(HistoryController.class);
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public List<HistoryRecord> findAll() {
        log.info("Fetching all history records");
        return historyService.findAll();
    }

    @GetMapping("/{id}")
    public HistoryRecord findById(@PathVariable Long id) {
        log.info("Fetching history record with id {}", id);
        return historyService.findById(id);
    }

    @PutMapping("/{id}")
    public HistoryRecord put(@PathVariable Long id, @RequestBody HistoryUpdateRequest request) {
        log.info("PUT update request received for history record {}", id);
        return update(id, request);
    }

    @PatchMapping("/{id}")
    public HistoryRecord patch(@PathVariable Long id, @RequestBody HistoryUpdateRequest request) {
        log.info("PATCH update request received for history record {}", id);
        return update(id, request);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        log.warn("Deleting all history records");
        historyService.deleteAll();
        log.info("All history records deleted successfully");
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.warn("Deleting history record with id {}", id);
        historyService.deleteById(id);
        log.info("History record {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

}