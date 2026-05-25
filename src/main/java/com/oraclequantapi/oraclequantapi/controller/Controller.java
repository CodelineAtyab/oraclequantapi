package com.oraclequantapi.oraclequantapi.controller;

import com.oraclequantapi.oraclequantapi.module.Sequence;
import com.oraclequantapi.oraclequantapi.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST layer — routes /sequenceDecoder requests to Service.
 * Contains zero business logic; all rules live in Service.
 */
@RestController
@RequestMapping("/sequenceDecoder")
public class Controller {

    @Autowired
    private Service service;

    // Submit a new sequence — validates input, decodes, and stores it
    @PostMapping
    public ResponseEntity<?> postSequence(@RequestBody Sequence sequence) {
        Sequence stored = service.addSequence(sequence);
        if (stored == null) {
            return ResponseEntity.badRequest().body("Input must only contain a-z and underscore, and must not start with underscore");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(stored);
    }

    // Update an existing enquiry by id — re-runs decoder on new input
    @PutMapping
    public ResponseEntity<?> updateSequence(@RequestBody Sequence sequence) {
        Sequence updated = service.updateSequence(sequence);
        if (updated == null) {
            return ResponseEntity.badRequest().body("Enquiry not found or input invalid");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    // Remove an enquiry by id
    @DeleteMapping
    public ResponseEntity<?> deleteSequence(@RequestBody Sequence sequence) {
        boolean deleted = service.deleteSequence(sequence.getId());
        if (!deleted) {
            return ResponseEntity.badRequest().body("Enquiry not found or already deleted");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Enquiry deleted successfully");
    }

    // Retrieve all stored sequence enquiries
    @GetMapping
    public ResponseEntity<List<Sequence>> getAllSequences() {
        return ResponseEntity.ok(service.getAllSequences());
    }

}
