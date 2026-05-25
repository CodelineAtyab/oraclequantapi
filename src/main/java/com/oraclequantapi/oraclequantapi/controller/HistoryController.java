package com.oraclequantapi.oraclequantapi.controller;
import com.oraclequantapi.oraclequantapi.model.MeasurementRecord;
import com.oraclequantapi.oraclequantapi.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
// All endpoints in this class start with "/history"
@RequestMapping(path = "/history")
public class HistoryController {

    @Autowired
    public HistoryService historyService;
//Returns all history records from the database as a JSON array
    @GetMapping
    public ResponseEntity<List<MeasurementRecord>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(historyService.getAll());
    }
//Returns a specific record by its ID
    @GetMapping(path = "/{id}")
    public ResponseEntity<MeasurementRecord> getById(@PathVariable long id) {
        return historyService.getById(id)
                .map(record -> ResponseEntity.status(HttpStatus.OK).body(record))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
//update input by specific ID
    @PutMapping(path = "/{id}")
    public ResponseEntity<MeasurementRecord> update(@PathVariable long id,
                                                    @RequestBody Map<String, String> body) {
        String newInput = body.get("input");
        return historyService.update(id, newInput)
                .map(record -> ResponseEntity.status(HttpStatus.OK).body(record))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
//Delete all the records
    @DeleteMapping
    public ResponseEntity<String> clearAll() {
        historyService.clearAll();
        return ResponseEntity.status(HttpStatus.OK).body("History has been Deleted");
    }
}
