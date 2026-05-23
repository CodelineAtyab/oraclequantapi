package com.oraclequantapi.oraclequantapi.service;

import com.oraclequantapi.oraclequantapi.model.ConversionHistory;
import com.oraclequantapi.oraclequantapi.repository.HistoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// This service handles all business logic related to history records
@Service
public class HistoryService {

    // Injected repository for database access
    private final HistoryRepository historyRepository;

    // Constructor injection (recommended over @Autowired)
    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    // Save a new history record to the database
    public ConversionHistory save(ConversionHistory record) {
        return historyRepository.save(record);
    }

    // Retrieve all history records
    public List<ConversionHistory> getAll() {
        return historyRepository.findAll();
    }

    // Retrieve a single record by its ID
    public Optional<ConversionHistory> getById(Long id) {
        return historyRepository.findById(id);
    }

    // Update an existing record — only updates fields that are not null
    public Optional<ConversionHistory> update(Long id, ConversionHistory updated) {
        return historyRepository.findById(id).map(existing -> {
            // Only overwrite fields if the new value is provided
            if (updated.getInput() != null) existing.setInput(updated.getInput());
            if (updated.getOutput() != null) existing.setOutput(updated.getOutput());
            if (updated.getSourceIpAddress() != null)
                existing.setSourceIpAddress(updated.getSourceIpAddress());
            return historyRepository.save(existing); // save updated record
        });
    }

    // Delete all history records from the database
    public void deleteAll() {
        historyRepository.deleteAll();
    }
}