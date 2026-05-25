package com.oraclequantapi.oraclequantapi.services;

import com.oraclequantapi.oraclequantapi.models.SequenceHistory;
import com.oraclequantapi.oraclequantapi.repositories.ConversionHistoryRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;

@Service
public class HistoryService {

    private static final Logger logger = LoggerFactory.getLogger(HistoryService.class);

    @Autowired
    private ConversionHistoryRepository historyRepository;

    public SequenceHistory save(SequenceHistory record){
        record.setId(UUID.randomUUID().toString());
        SequenceHistory saved = historyRepository.save(record);
        logger.info("History record saved: {}", saved.getId());
        return saved;
    }

    public List<SequenceHistory> getAll(){
        logger.info("Fetching all history record");
        return historyRepository.findAll();
    }

    public Optional<SequenceHistory> getById(String id){
        logger.info("Fetching history record: {}", id);
        return historyRepository.findById(id);
    }


    public Optional<SequenceHistory> update(String id, SequenceHistory updated) {  // ← FIXED
        return historyRepository.findById(id).map(existing -> {
            existing.setSourceIpAddress(updated.getSourceIpAddress());
            existing.setInput(updated.getInput());
            existing.setOutput(updated.getOutput());
            existing.setTimestamp(updated.getTimestamp());
            logger.info("History record fully updated: {}", id);
            return historyRepository.save(existing);
        });
    }

    public Optional<SequenceHistory> patch(String id, SequenceHistory patch) {  // ← FIXED
        return historyRepository.findById(id).map(existing -> {
            if (patch.getSourceIpAddress() != null) existing.setSourceIpAddress(patch.getSourceIpAddress());
            if (patch.getInput()           != null) existing.setInput(patch.getInput());
            if (patch.getOutput()          != null) existing.setOutput(patch.getOutput());
            if (patch.getTimestamp()       != null) existing.setTimestamp(patch.getTimestamp());
            logger.info("History record patched: {}", id);
            return historyRepository.save(existing);
        });
    }

    public void deleteAll() {
        logger.warn("Clearing ALL history records");
        historyRepository.deleteAll();
    }

    public void deleteById(String id) {
        logger.warn("Deleting history record: {}", id);
        historyRepository.deleteById(id);
    }
}
