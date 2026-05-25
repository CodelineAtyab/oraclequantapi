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
/**
 * Web endpoints for viewing and editing saved conversion history.
 *
 * All URLs in this controller start with /history because of @RequestMapping.
 */
public class HistoryController {

    // Service that contains the actual history logic and database calls.
    private final HistoryService historyService;

    /**
     * Handles: GET /history
     * Returns every saved conversion history record.
     */
    @GetMapping
    public ResponseEntity<List<HistoryRecord>> getAll() {
        log.info("GET /history called");
        return ResponseEntity.ok(historyService.getAll());
    }

    /**
     * Handles: GET /history/{id}
     * Returns one history record by its database id, or 404 if it does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HistoryRecord> getById(@PathVariable Long id) {
        log.info("GET /history/{} called", id);
        return historyService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Handles: PUT /history/{id}
     * Replaces the editable values on an existing history record.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HistoryRecord> update(
            @PathVariable Long id,
            @RequestBody HistoryRecord record) {
        log.info("PUT /history/{} called", id);
        return ResponseEntity.ok(historyService.update(id, record));
    }

    /**
     * Handles: PATCH /history/{id}
     * Updates only the fields sent in the request body, such as input or output.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<HistoryRecord> partialUpdate(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        log.info("PATCH /history/{} called", id);
        return ResponseEntity.ok(historyService.partialUpdate(id, updates));
    }

    /**
     * Handles: DELETE /history
     * Deletes all saved history rows.
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        log.info("DELETE /history called");
        historyService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
