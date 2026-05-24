package com.oraclequantapi.oraclequantapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oraclequantapi.oraclequantapi.model.HistoryRecord;
import com.oraclequantapi.oraclequantapi.repository.HistoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class HistoryService {
    private final HistoryRepository repository;
    private final ObjectMapper objectMapper;

    public HistoryService(HistoryRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public HistoryRecord record(String input, List<Integer> output, String sourceIpAddress) {

        try {
            String outputJson = objectMapper.writeValueAsString(output);
            return repository.save(new HistoryRecord(sourceIpAddress, input, outputJson));
        }
        catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to serialize conversion output", exception);
        }
    }

    @Transactional(readOnly = true)
    public List<HistoryRecord> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional(readOnly = true)
    public HistoryRecord findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new HistoryRecordNotFoundException(id));
    }

    @Transactional
    public HistoryRecord update(Long id, Instant timestamp, String sourceIpAddress, String input, String output) {
        HistoryRecord record = findById(id);

        if (timestamp != null) {
            record.setTimestamp(timestamp);
        }

        if (sourceIpAddress != null) {
            record.setSourceIpAddress(sourceIpAddress);
        }

        if (input != null) {
            record.setInput(input);
        }

        if (output != null) {
            record.setOutput(output);
        }

        return repository.save(record);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAllInBatch();
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new HistoryRecordNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public static class HistoryRecordNotFoundException extends RuntimeException {
        public HistoryRecordNotFoundException(Long id) {
            super("History record not found: " + id);
        }
    }

}