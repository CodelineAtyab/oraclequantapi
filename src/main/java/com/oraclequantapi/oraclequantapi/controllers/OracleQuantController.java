package com.oraclequantapi.oraclequantapi.controllers;

import com.oraclequantapi.oraclequantapi.models.OracleQuantRecord;
import com.oraclequantapi.oraclequantapi.services.OracleQuantService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/convert-measurements")
public class OracleQuantController {

    private static final Logger log = LoggerFactory.getLogger(OracleQuantController.class);

    private final OracleQuantService service;

    public OracleQuantController(OracleQuantService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Integer>> convert(@RequestParam("input") String input,
                                                  HttpServletRequest request) {
        String sourceIp = request.getRemoteAddr();
        log.info("Conversion request - input: \"{}\", source IP: {}", input, sourceIp);
        List<Integer> result = service.convertMeasurements(input, sourceIp);
        if (result == null) {
            log.warn("Invalid input, returning 400 - input: \"{}\"", input);
            return ResponseEntity.badRequest().build();
        }
        log.info("Conversion result - input: \"{}\", output: {}", input, result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history")
    public ResponseEntity<List<OracleQuantRecord>> getAllHistory() {
        log.info("Fetching all history records");
        List<OracleQuantRecord> records = service.getAllHistory();
        log.info("Fetched {} history records", records.size());
        return ResponseEntity.ok(records);
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<OracleQuantRecord> getHistoryById(@PathVariable Integer id) {
        log.info("Fetching history record by id: {}", id);
        return service.getHistoryById(id)
                .map(record -> {
                    log.info("Found history record: {}", id);
                    return ResponseEntity.ok(record);
                })
                .orElseGet(() -> {
                    log.warn("History record not found: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PutMapping("/history/{id}")
    public ResponseEntity<OracleQuantRecord> updateHistory(@PathVariable Integer id,
                                                            @RequestBody OracleQuantRecord record) {
        log.info("Updating history record: {}", id);
        OracleQuantRecord updated = service.updateHistory(id, record);
        log.info("Updated history record: {}", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/history")
    public ResponseEntity<Void> clearHistory() {
        log.info("Clearing all history records");
        service.clearHistory();
        log.info("All history records cleared");
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/history/{id}")
    public ResponseEntity<Void> deleteHistory(@PathVariable Integer id) {
        log.info("Deleting history record: {}", id);
        service.deleteHistoryById(id);
        log.info("Deleted history record: {}", id);
        return ResponseEntity.noContent().build();
    }
}
