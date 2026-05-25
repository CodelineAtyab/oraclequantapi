package com.oraclequantapi.oraclequantapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oraclequantapi.oraclequantapi.model.HistoryRecord;
import com.oraclequantapi.oraclequantapi.repository.HistoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service // Marks this class as a Spring service
public class HistoryService {

    private final HistoryRepository repository; // Repository used for database operations
    private final ObjectMapper objectMapper; // ObjectMapper used to convert Java objects into JSON

    // Constructor dependency injection
    public HistoryService(HistoryRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    // Saves conversion history into the database
    @Transactional
    public HistoryRecord record(String input, List<Integer> output, String sourceIpAddress) {
        try {
            String outputJson = objectMapper.writeValueAsString(output);
            return repository.save(new HistoryRecord(sourceIpAddress, input, outputJson));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to serialize conversion output", exception);
        }
    }

    // Returns all history records sorted by ID
    @Transactional(readOnly = true)
    public List<HistoryRecord> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    // Finds history record using ID
    @Transactional(readOnly = true)
    public HistoryRecord findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new HistoryRecordNotFoundException(id));
    }

    // Updates existing history record
    @Transactional
    public HistoryRecord update(
            Long id,
            LocalDateTime timestamp,
            String sourceIpAddress,
            String input,
            String output
    ) {
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

    // Deletes all history records
    @Transactional
    public void deleteAll() {
        repository.deleteAllInBatch();
    }

    // Deletes history record using ID
    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new HistoryRecordNotFoundException(id);
        }

        repository.deleteById(id);
    }

    // Custom exception for missing history records
    public static class HistoryRecordNotFoundException extends RuntimeException {
        public HistoryRecordNotFoundException(Long id) {
            super("History record not found: " + id);
        }
    }
}