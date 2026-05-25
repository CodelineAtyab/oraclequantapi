package com.oraclequantapi.oraclequantapi.controllers;

import com.oraclequantapi.oraclequantapi.models.HistoryRecord;
import com.oraclequantapi.oraclequantapi.services.HistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public List<HistoryRecord> getAllHistory() {
        return historyService.getAllHistory();
    }

    @GetMapping("/{id}")
    public HistoryRecord getHistoryById(@PathVariable Long id) {
        return historyService.getHistoryById(id);
    }

    @PutMapping("/{id}")
    public HistoryRecord updateHistory(@PathVariable Long id,
                                       @RequestBody HistoryRecord updatedRecord) {
        return historyService.updateHistory(id, updatedRecord);
    }

    @DeleteMapping
    public String clearHistory() {
        historyService.clearHistory();
        return "History cleared successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteHistoryById(@PathVariable Long id) {
        historyService.deleteHistoryById(id);
        return "History record deleted successfully";
    }
}