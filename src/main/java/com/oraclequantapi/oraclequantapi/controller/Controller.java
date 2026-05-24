package com.oraclequantapi.oraclequantapi.controller;

import com.oraclequantapi.oraclequantapi.module.Sequence;
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
    public ResponseEntity<?> postSequence(@RequestBody Sequence sequence) {
        Sequence stored = service.addSequence(sequence);
        if (stored == null) {
            return ResponseEntity.badRequest().body("Input must only contain a-z and underscore, and must not start with underscore");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(stored);
    }

    // Returns all previously submitted sequence enquiries
    @GetMapping
    public ResponseEntity<List<Sequence>> getAllSequences() {
        return ResponseEntity.ok(service.getAllSequences());
    }

}
