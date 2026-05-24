package com.oraclequantapi.oraclequantapi.controller;

import com.oraclequantapi.oraclequantapi.service.Sequence;
import com.oraclequantapi.oraclequantapi.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sequenceDecoder")
public class Controller {

    @Autowired
    private Service service;

    // Accepts a sequence enquiry and stores it in memory
    @PostMapping
    public ResponseEntity<Sequence> postSequence(@RequestBody Sequence sequence) {
        Sequence stored = service.addSequence(sequence);
        return ResponseEntity.status(HttpStatus.CREATED).body(stored);
    }

    // Returns all previously submitted sequence enquiries
    @GetMapping
    public ResponseEntity<List<Sequence>> getAllSequences() {
        return ResponseEntity.ok(service.getAllSequences());
    }

}
