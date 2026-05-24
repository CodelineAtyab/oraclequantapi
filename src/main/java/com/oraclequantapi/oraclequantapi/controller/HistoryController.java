package com.oraclequantapi.oraclequantapi.controller;

import com.oraclequantapi.oraclequantapi.model.HistoryRecord;
import com.oraclequantapi.oraclequantapi.service.HistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")

public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    //Get all records
    //http://localhost:8080/history
    @GetMapping
    public List<HistoryRecord> getAll (){
        return historyService.getAll();
    }

    //Get one record  by id
    // http://localhost:8080/history/1
    @GetMapping("/{id}")
    public ResponseEntity<HistoryRecord>getById(@PathVariable Long id){
        return historyService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    //put - full update by ID
    //http://localhost:8080/history/1

    @PutMapping("/{id}")
    public ResponseEntity<HistoryRecord> put(
            @PathVariable Long id,
            @RequestBody HistoryRecord record) {
        return ResponseEntity.ok(historyService.update(id, record));
    }

    //patch - partial update by ID
    //http://localhost:8080/history/1
    @PatchMapping("/{id}")
    public ResponseEntity<HistoryRecord> patch(
            @PathVariable Long id,
            @RequestBody HistoryRecord record) {
        return ResponseEntity.ok(historyService.update(id, record));
    }



    //DELETE - clear all history
    //http://localhost:8080/history
    @DeleteMapping
    public ResponseEntity<String> deleteAll() {

        historyService.deleteAll();

        return ResponseEntity.ok("all records History deleted");
    }
}
