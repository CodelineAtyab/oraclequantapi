package com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.controller;

import com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.model.HistoryRecord;
import com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLOutput;
import java.util.List;


 // Web layer — CRUD-style REST API for persisted conversion history.
@RestController
@RequestMapping("/history")
public class HistoryController {

    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    // GET /history — list every saved request (newest order depends on DB default). */
    @GetMapping
    public ResponseEntity<List<HistoryRecord>> getAllHistory() {
        List<HistoryRecord> history = historyService.getAllRecords();
        return ResponseEntity.ok(history);
    }

    // GET /history/{id} — fetch one record; 404 if id does not exist. */
    @GetMapping("/{id}")
    public ResponseEntity<HistoryRecord> getHistoryById(@PathVariable Long id) {
        HistoryRecord record = historyService.getRecordById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "History record not found with ID " + id));
        return ResponseEntity.ok(record);
    }

    // PUT /history/{id} — replace fields on an existing row (full update). */
    @PutMapping("/{id}")
    public ResponseEntity<HistoryRecord> updateHistory(
            @PathVariable Long id,
            @RequestBody HistoryRecord updatedRecord) {
        HistoryRecord saved = historyService.updateRecord(id, updatedRecord);
        return ResponseEntity.ok(saved);
    }

    // PATCH /history/{id} — change only fields sent in JSON body (partial update). */
    @PatchMapping("/{id}")
    public ResponseEntity<HistoryRecord> patchHistory(
            @PathVariable Long id,
            @RequestBody HistoryRecord partialRecord) {
        HistoryRecord saved = historyService.patchRecord(id, partialRecord);
        return ResponseEntity.ok(saved);
    }

    // DELETE /history — wipe entire history table (204 No Content). */
    @DeleteMapping
    public ResponseEntity<Void> clearAllHistory() {
        historyService.clearHistory();
        return ResponseEntity.noContent().build();
    }
}
