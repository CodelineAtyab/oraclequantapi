package com.example.pkc_api.controller;


import com.example.pkc_api.dto.UpdateHistoryRequest;
import com.example.pkc_api.entity.SequenceHistory;
import com.example.pkc_api.service.HistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public List<SequenceHistory> getAllHistory() {
        return historyService.getAllHistory();
    }

    @GetMapping("/{id}")
    public SequenceHistory getHistoryById(@PathVariable UUID id) {
        return historyService.getHistoryById(id);
    }

    @PatchMapping("/{id}")
    public SequenceHistory updateHistory(
            @PathVariable UUID id,
            @RequestBody UpdateHistoryRequest request
    ) {
        return historyService.updateHistory(id, request);
    }

    @PutMapping("/{id}")
    public SequenceHistory replaceHistory(
            @PathVariable UUID id,
            @RequestBody UpdateHistoryRequest request
    ) {
        return historyService.updateHistory(id, request);
    }

    @DeleteMapping
    public String clearHistory() {
        historyService.clearHistory();
        return "History cleared successfully.";
    }
}
