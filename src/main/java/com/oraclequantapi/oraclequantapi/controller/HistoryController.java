package com.oraclequantapi.oraclequantapi.controller;

import com.oraclequantapi.oraclequantapi.model.HistoryRecord;
import com.oraclequantapi.oraclequantapi.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {
    private static final Logger log = LoggerFactory.getLogger(HistoryController.class);
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public List<HistoryRecord> findAll() {
        log.info("Fetching all history records");
        return historyService.findAll();
    }

    @GetMapping("/{id}")
    public HistoryRecord findById(@PathVariable Long id) {
        log.info("Fetching history record with id {}", id);
        return historyService.findById(id);
    }

}