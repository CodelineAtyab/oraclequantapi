package com.oraclequantapi.oraclequantapi.controller;

import com.oraclequantapi.oraclequantapi.model.HistoryRecord;
import com.oraclequantapi.oraclequantapi.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
@Slf4j
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<List<HistoryRecord>> getAll() {
        log.info("GET /history called");
        return ResponseEntity.ok(historyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoryRecord> getById(@PathVariable Long id) {
        log.info("GET /history/{} called", id);
        return historyService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoryRecord> update(
            @PathVariable Long id,
            @RequestBody HistoryRecord record) {
        log.info("PUT /history/{} called", id);
        return ResponseEntity.ok(historyService.update(id, record));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HistoryRecord> partialUpdate(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        log.info("PATCH /history/{} called", id);
        return ResponseEntity.ok(historyService.partialUpdate(id, updates));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        log.info("DELETE /history called");
        historyService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
