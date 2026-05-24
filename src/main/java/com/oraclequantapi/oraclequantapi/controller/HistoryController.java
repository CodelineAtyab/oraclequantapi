package com.oraclequantapi.oraclequantapi.controller;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oraclequantapi.oraclequantapi.model.HistoryRecord;
import com.oraclequantapi.oraclequantapi.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

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

    @ExceptionHandler(HistoryService.HistoryRecordNotFoundException.class)
    public ResponseEntity<Map<String, Object>> historyNotFound(HistoryService.HistoryRecordNotFoundException exception) {
        log.warn("History record not found: {}", exception.getMessage());
        return error(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    private HistoryRecord update(Long id, HistoryUpdateRequest request) {
        HistoryRecord record = historyService.update(id, request.timestamp(), request.sourceIpAddress(), request.input(), request.output());
        log.info("History record {} updated successfully", id);
        return record;
    }

    private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("timestamp", Instant.now().toString(), "status", status.value(), "error", status.getReasonPhrase(), "message", message));
    }

    public record HistoryUpdateRequest (Instant timestamp, @JsonProperty("source_ip_address") @JsonAlias("sourceIpAddress") String sourceIpAddress, String input, String output) {
    }


}