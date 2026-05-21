package com.oraclequantapi.oraclequantapi.service;

import com.oraclequantapi.oraclequantapi.model.HistoryRecord;
import com.oraclequantapi.oraclequantapi.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryService {

    private final HistoryRepository repository;

    @Transactional
    public HistoryRecord save(String input, String output, String sourceIpAddress) {
        log.info("Saving history record for input={}, ip={}", input, sourceIpAddress);
        HistoryRecord record = new HistoryRecord();
        record.setTimestamp(LocalDateTime.now());
        record.setInput(input);
        record.setOutput(output);
        record.setSourceIpAddress(sourceIpAddress);
        return repository.save(record);
    }

    @Transactional(readOnly = true)
    public List<HistoryRecord> getAll() {
        log.info("Fetching all history records");
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<HistoryRecord> getById(Long id) {
        log.info("Fetching history record by id={}", id);
        return repository.findById(id);
    }

    @Transactional
    public HistoryRecord update(Long id, HistoryRecord updated) {
        log.info("Updating history record id={}", id);
        HistoryRecord existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("History record not found with id: " + id));
        existing.setInput(updated.getInput());
        existing.setOutput(updated.getOutput());
        return repository.save(existing);
    }

    @Transactional
    public HistoryRecord partialUpdate(Long id, Map<String, Object> updates) {
        log.info("Partial updating history record id={}", id);
        HistoryRecord existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("History record not found with id: " + id));
        updates.forEach((key, value) -> {
            if ("input".equals(key)) {
                existing.setInput((String) value);
            } else if ("output".equals(key)) {
                existing.setOutput((String) value);
            }
        });
        return repository.save(existing);
    }

    @Transactional
    public void deleteAll() {
        log.info("Deleting all history records");
        repository.deleteAll();
    }
}
