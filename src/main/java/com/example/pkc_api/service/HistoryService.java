package com.example.pkc_api.service;


import com.example.pkc_api.dto.UpdateHistoryRequest;
import com.example.pkc_api.entity.SequenceHistory;
import com.example.pkc_api.exception.HistoryRecordNotFoundException;
import com.example.pkc_api.repository.SequenceHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class HistoryService {

    private static final Logger log = LoggerFactory.getLogger(HistoryService.class);

    private final SequenceHistoryRepository historyRepository;

    public HistoryService(SequenceHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public SequenceHistory saveHistory(String input, String output) {
        SequenceHistory history = new SequenceHistory(
                LocalDateTime.now(),
                input,
                output
        );

        SequenceHistory savedHistory = historyRepository.save(history);
        log.info("Saved history record id={}", savedHistory.getId());
        return savedHistory;
    }

    public List<SequenceHistory> getAllHistory() {
        return historyRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public SequenceHistory getHistoryById(UUID id) {
        return historyRepository.findById(id)
                .orElseThrow(() -> new HistoryRecordNotFoundException(id));
    }

    public SequenceHistory updateHistory(UUID id, UpdateHistoryRequest request) {
        SequenceHistory history = getHistoryById(id);

        if (request.getInput() != null) {
            history.setInput(request.getInput());
        }

        if (request.getOutput() != null) {
            history.setOutput(request.getOutput());
        }

        SequenceHistory savedHistory = historyRepository.save(history);
        log.info("Updated history record id={}", id);
        return savedHistory;
    }

    public void clearHistory() {
        historyRepository.deleteAll();
        log.info("Cleared all history records");
    }
}
