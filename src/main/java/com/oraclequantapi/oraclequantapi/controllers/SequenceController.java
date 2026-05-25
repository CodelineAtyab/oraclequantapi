package com.oraclequantapi.oraclequantapi.controllers;
import com.oraclequantapi.oraclequantapi.models.Sequence;
import com.oraclequantapi.oraclequantapi.models.SequenceHistory;
import com.oraclequantapi.oraclequantapi.services.SequenceService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sequences")
public class SequenceController {
    private static final Logger log = LoggerFactory.getLogger(SequenceController.class);

    private final SequenceService service;

    public SequenceController(SequenceService service) {
        this.service = service;
    }

    // =========================
    // CONVERT ENDPOINT
    // =========================
    @GetMapping("/convert-measurements")
    public ResponseEntity<List<Integer>> convertMeasurements(
            @RequestParam("input") String input,
            HttpServletRequest request) {

        String sourceIp = request.getRemoteAddr();

        log.info("Conversion request - input: \"{}\", source IP: {}", input, sourceIp);

        Sequence sequence = new Sequence(input);

        if (!sequence.is_valid()) {
            log.warn("Invalid input received: \"{}\"", input);
            return ResponseEntity.badRequest().build();
        }

        List<Integer> result = service.process_sequence(sequence, sourceIp);

        log.info("Conversion result - input: \"{}\", output: {}", input, result);

        return ResponseEntity.ok(result);
    }

    // =========================
    // HISTORY - GET ALL
    // =========================
    @GetMapping("/history")
    public ResponseEntity<List<SequenceHistory>> getAllHistory() {

        log.info("Fetching all history records");

        List<SequenceHistory> records = service.getAllHistory();

        log.info("Fetched {} history records", records.size());

        return ResponseEntity.ok(records);
    }

    // =========================
    // HISTORY - GET BY ID
    // =========================
    @GetMapping("/history/{id}")
    public ResponseEntity<SequenceHistory> getHistoryById(@PathVariable Long id) {

        log.info("Fetching history record by id: {}", id);
        return service.getHistoryById(id).map(record -> {
                    log.info("Found history record: {}", id);
                    return ResponseEntity.ok(record);
        })
                .orElseGet(() -> {
                    log.warn("History record not found: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // =========================
    // HISTORY - UPDATE
    // =========================
    @PutMapping("/history/{id}")
    public ResponseEntity<SequenceHistory> updateHistory(
            @PathVariable Long id,
            @RequestBody SequenceHistory record) {

        log.info("Updating history record: {}", id);

        try {
            SequenceHistory updated = service.updateHistory(id, record);
            log.info("Updated history record: {}", id);
            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {
            log.warn("Update failed - record not found: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // =========================
    // HISTORY - DELETE ALL
    // =========================
    @DeleteMapping("/history")
    public ResponseEntity<Void> clearHistory() {

        log.info("Clearing all history records");

        service.deleteHistory();

        log.info("All history records cleared");

        return ResponseEntity.noContent().build();
    }

    // =========================
    // HISTORY - DELETE BY ID (ADDED like sample solution)
    // =========================
    @DeleteMapping("/history/{id}")
    public ResponseEntity<Void> deleteHistoryById(@PathVariable Long id) {

        log.info("Deleting history record: {}", id);

        boolean exists = service.getHistoryById(id).isPresent();

        if (!exists) {
            log.warn("Delete failed - record not found: {}", id);
            return ResponseEntity.notFound().build();
        }

        service.deleteHistoryById(id);

        log.info("Deleted history record: {}", id);

        return ResponseEntity.noContent().build();
    }
}
