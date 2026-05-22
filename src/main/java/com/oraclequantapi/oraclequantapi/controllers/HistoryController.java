package com.oraclequantapi.oraclequantapi.controllers;

import com.oraclequantapi.oraclequantapi.models.MeasurementHistory;
import com.oraclequantapi.oraclequantapi.services.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping (path = "/history")
public class HistoryController {
    @Autowired
    public HistoryService historyService;

    // Return all saved conversion requests from Oracle DB.
    @GetMapping
    public ResponseEntity<List<MeasurementHistory>> getAllHistory() {
        return ResponseEntity.status(HttpStatus.OK).body(historyService.getHistory());
    }

    // Return one saved conversion request by id.
    @GetMapping(path = "/{id}")
    public ResponseEntity<MeasurementHistory> getSpecificHistory(@PathVariable String id) {
        MeasurementHistory history = historyService.getHistoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(history);
    }

    // Update a history record by id.
    @PutMapping(path = "/{id}")
    public ResponseEntity<MeasurementHistory> updateHistory(
            @PathVariable String id,
            @RequestBody MeasurementHistory incomingHistory
    ) {
        MeasurementHistory history = historyService.updateHistory(id, incomingHistory);
        return ResponseEntity.status(HttpStatus.OK).body(history);
    }

    // Delete all saved history records.
    @DeleteMapping
    public ResponseEntity<Void> clearHistory() {
        historyService.clearHistory();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
