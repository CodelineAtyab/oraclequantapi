package com.oraclequantapi.oraclequantapi.controller;

import com.oraclequantapi.oraclequantapi.repository.DATABASE;
import com.oraclequantapi.oraclequantapi.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST entry point for /sequenceDecoder; delegates all logic to Service, contains no business rules.
@RestController
@RequestMapping("/sequenceDecoder")
public class Controller {

    @Autowired
    private Service service;

    // Accepts a sequence enquiry; rejects with 400 if input contains non a-z or non-underscore characters
    @PostMapping
    public ResponseEntity<?> postSequence(@RequestBody DATABASE db) {
        DATABASE stored = service.addSequence(db);
        if (stored == null) {
            return ResponseEntity.badRequest().body("Input must only contain a-z and underscore, and must not start with underscore");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(stored);
    }

    // Updates an existing enquiry by id; re-runs decoder on new input
    @PutMapping
    public ResponseEntity<?> updateSequence(@RequestBody DATABASE db) {
        DATABASE updated = service.updateSequence(db);
        if (updated == null) {
            return ResponseEntity.badRequest().body("Enquiry not found or input invalid");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    // Removes an existing enquiry by id from Oracle
    @DeleteMapping
    public ResponseEntity<?> deleteSequence(@RequestBody DATABASE db) {
        boolean deleted = service.deleteSequence(db.getId());
        if (!deleted) {
            return ResponseEntity.badRequest().body("Enquiry not found or already deleted");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Enquiry deleted successfully");
    }

    // Returns all previously submitted sequence enquiries
    @GetMapping
    public ResponseEntity<List<DATABASE>> getAllSequences() {
        return ResponseEntity.ok(service.getAllSequences());
    }

}
