package com.oraclequantapi.oraclequantapi.controllers;

import com.oraclequantapi.oraclequantapi.models.OracleQuantRecord;
import com.oraclequantapi.oraclequantapi.services.OracleQuantService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/convert-measurements")
public class OracleQuantController {

    private final OracleQuantService service;

    public OracleQuantController(OracleQuantService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Integer>> convert(@RequestParam("input") String input,
                                                  HttpServletRequest request) {
        List<Integer> result = service.convertMeasurements(input, request.getRemoteAddr());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history")
    public ResponseEntity<List<OracleQuantRecord>> getAllHistory() {
        return ResponseEntity.ok(service.getAllHistory());
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<OracleQuantRecord> getHistoryById(@PathVariable Integer id) {
        return service.getHistoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/history/{id}")
    public ResponseEntity<OracleQuantRecord> updateHistory(@PathVariable Integer id,
                                                            @RequestBody OracleQuantRecord record) {
        OracleQuantRecord updated = service.updateHistory(id, record);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/history")
    public ResponseEntity<Void> clearHistory() {
        service.clearHistory();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/history/{id}")
    public ResponseEntity<Void> deleteHistory(@PathVariable Integer id) {
        service.deleteHistoryById(id);
        return ResponseEntity.noContent().build();
    }
}
