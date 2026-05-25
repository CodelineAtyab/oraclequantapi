package com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.service;

import com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.model.HistoryRecord;
import com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.repository.HistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

 // Business logic for history endpoints
 // Sits between HistoryController and HistoryRepository
@Service
public class HistoryServiceImpl implements HistoryService {

    private static final Logger logger = LoggerFactory.getLogger(HistoryServiceImpl.class);

    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public List<HistoryRecord> getAllRecords() {
        logger.info("Fetching all history records from database.");
        return historyRepository.findAll();
    }

    @Override
    public Optional<HistoryRecord> getRecordById(Long id) {
        logger.info("Fetching history record by ID: {}", id);
        return historyRepository.findById(id);
    }

    @Override
    public HistoryRecord updateRecord(Long id, HistoryRecord updatedRecord) {
        logger.info("Completely updating history record with ID: {}", id);
        return historyRepository.findById(id)
                .map(existing -> {

                    // overwrite each field when provided and others keep old value
                    existing.setTimestamp(updatedRecord.getTimestamp() != null ? updatedRecord.getTimestamp() : existing.getTimestamp());
                    existing.setSourceIpAddress(updatedRecord.getSourceIpAddress() != null ? updatedRecord.getSourceIpAddress() : existing.getSourceIpAddress());
                    existing.setInput(updatedRecord.getInput() != null ? updatedRecord.getInput() : existing.getInput());
                    existing.setOutput(updatedRecord.getOutput() != null ? updatedRecord.getOutput() : existing.getOutput());
                    HistoryRecord saved = historyRepository.save(existing);
                    logger.debug("Successfully updated history record: {}", id);
                    return saved;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "History record not found with ID " + id));
    }

    @Override
    public HistoryRecord patchRecord(Long id, HistoryRecord partialRecord) {
        logger.info("Partially patching history record with ID: {}", id);
        return historyRepository.findById(id)
                .map(existing -> {

                    // only not null fields from JSON body are applied
                    if (partialRecord.getTimestamp() != null) {
                        existing.setTimestamp(partialRecord.getTimestamp());
                    }
                    if (partialRecord.getSourceIpAddress() != null) {
                        existing.setSourceIpAddress(partialRecord.getSourceIpAddress());
                    }
                    if (partialRecord.getInput() != null) {
                        existing.setInput(partialRecord.getInput());
                    }
                    if (partialRecord.getOutput() != null) {
                        existing.setOutput(partialRecord.getOutput());
                    }
                    HistoryRecord saved = historyRepository.save(existing);
                    logger.debug("Successfully patched history record: {}", id);
                    return saved;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "History record not found with ID " + id));
    }

    @Override
    public void clearHistory() {
        logger.warn("Clearing all history records from the database!");
        historyRepository.deleteAll(); // removes every row and table structure remains
        logger.info("All history records cleared successfully.");
    }
}
