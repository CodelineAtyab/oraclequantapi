package com.oraclequantapi.oraclequantapi.controllers;


import com.oraclequantapi.oraclequantapi.models.SequenceHistory;
import com.oraclequantapi.oraclequantapi.services.HistoryService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;

@RestController
@RequestMapping("/input")
public class HistoryController {

    private static final Logger logger = LoggerFactory.getLogger(HistoryController.class);

    @Autowired
    private HistoryService historyService;

    @GetMapping
    public ResponseEntity<List<SequenceHistory>> getAll() {
        logger.info("GET /input");
        return ResponseEntity.ok(historyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SequenceHistory> getById(@PathVariable String id) {
        logger.info("GET /input/{}", id);
        return historyService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

        @PutMapping("/{id}")
        public ResponseEntity<SequenceHistory> update (
                @PathVariable String id,
                @RequestBody SequenceHistory updated) {
            logger.info("PUT /input/{}", id);
            return historyService.update(id, updated)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        @PatchMapping("/{id}")
        public ResponseEntity<SequenceHistory> patch (
                @PathVariable String id,
                @RequestBody SequenceHistory patch){
            logger.info("PATCH /input/{}", id);
            return historyService.patch(id, patch)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        //delete all records
        @DeleteMapping
        public ResponseEntity<Void> deleteAll () {
            logger.warn("DELETE /input — clearing all records");
            historyService.deleteAll();
            return ResponseEntity.noContent().build();
        }

        //delete one records
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteById (@PathVariable String id){
            logger.warn("DELETE /input/{}", id);
            if (historyService.getById(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            historyService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
