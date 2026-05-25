package com.oraclequantapi.oraclequantapi.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import com.oraclequantapi.oraclequantapi.model.Sequence;
import com.oraclequantapi.oraclequantapi.service.SequenceService;

@RestController
@RequestMapping("/api")
public class SequenceController {

    private final SequenceService sequenceService;

    public SequenceController(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @GetMapping("/convert-measurements")
    public ResponseEntity<List<Integer>> GET(@RequestParam("input") String input) {
        Sequence currentSequence = sequenceService.get_sequence(input);

        if (!currentSequence.is_valid()) {
            return ResponseEntity.badRequest().build();
        }

        List<Integer> decodedList = sequenceService.process_sequence(currentSequence);
        return ResponseEntity.ok(decodedList);
    }
}
