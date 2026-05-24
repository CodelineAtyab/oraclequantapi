package com.oraclequantapi.oraclequantapi.controllers;


import com.oraclequantapi.oraclequantapi.models.Sequence;
import com.oraclequantapi.oraclequantapi.models.SequenceHistory;
import com.oraclequantapi.oraclequantapi.services.SequenceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SequenceController {

    private final SequenceService sequenceService;

    public SequenceController(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    // Endpoint maps directly to: GET /api/v1/convert-measurements?input=XYZ
    @GetMapping("/convert-measurements")
    public ResponseEntity<?> convertMeasurements(
            @RequestParam("input") String rawInput,
            HttpServletRequest request) {

        Sequence sequence = new Sequence(rawInput);

        if (!sequence.is_valid()) {
            return ResponseEntity.badRequest().body("Validation failed: Strings must contain characters between 'a' and 'z' or '_' only.");
        }

        String clientIp = request.getRemoteAddr();
        List<Integer> result = sequenceService.process_sequence(sequence, clientIp);

        return ResponseEntity.ok(result);
    }

    // History Read All
    @GetMapping("/history")
    public List<SequenceHistory> getAllHistory() {
        return sequenceService.getAllHistory();
    }

    // History Read Single Item
    @GetMapping("/history/{id}")
    public ResponseEntity<SequenceHistory> getHistoryById(@PathVariable Long id) {
        return sequenceService.getHistoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // History Update
    @PutMapping("/history/{id}")
    public ResponseEntity<SequenceHistory> updateHistory(@PathVariable Long id, @RequestBody SequenceHistory updatedData) {
        try {
            return ResponseEntity.ok(sequenceService.updateHistory(id, updatedData));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // History Wipeout Clear
    @DeleteMapping("/history")
    public ResponseEntity<Void> clearHistory() {
        sequenceService.deleteHistory();
        return ResponseEntity.noContent().build();
    }
}
