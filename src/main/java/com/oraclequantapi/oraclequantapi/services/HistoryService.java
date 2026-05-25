package com.oraclequantapi.oraclequantapi.services;

import com.oraclequantapi.oraclequantapi.models.HistoryRecord;
import com.oraclequantapi.oraclequantapi.repositories.HistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public void saveHistory(String input, String output, String sourceIpAddress) {

        HistoryRecord record = new HistoryRecord();

        record.setInput(input);
        record.setOutput(output);
        record.setSourceIpAddress(sourceIpAddress);
        record.setTimestamp(LocalDateTime.now().toString());

        historyRepository.save(record);
    }

    public List<HistoryRecord> getAllHistory() {
        return historyRepository.findAll();
    }

    public HistoryRecord getHistoryById(Long id) {
        return historyRepository.findById(id).orElse(null);
    }

    public HistoryRecord updateHistory(Long id, HistoryRecord updatedRecord) {

        HistoryRecord existingRecord = historyRepository.findById(id).orElse(null);

        if (existingRecord != null) {
            existingRecord.setInput(updatedRecord.getInput());
            existingRecord.setOutput(updatedRecord.getOutput());
            existingRecord.setSourceIpAddress(updatedRecord.getSourceIpAddress());
            existingRecord.setTimestamp(updatedRecord.getTimestamp());

            return historyRepository.save(existingRecord);
        }

        return null;
    }

    public void clearHistory() {
        historyRepository.deleteAll();
    }

    public void deleteHistoryById(Long id) {
        historyRepository.deleteById(id);
    }
}