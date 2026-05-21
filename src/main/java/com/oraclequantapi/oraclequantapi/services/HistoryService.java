package com.oraclequantapi.oraclequantapi.services;

import com.oraclequantapi.oraclequantapi.models.MeasurementHistory;
import com.oraclequantapi.oraclequantapi.repositories.MeasurementHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    @Autowired
    private MeasurementHistoryRepository measurementHistoryRepository;

    // Save one conversion request into Oracle DB
    public MeasurementHistory saveCurrentMeasurement(
            String sourceIpAddress,
            MeasurementSequence sequence,
            List<Long> output
    ) {
        MeasurementHistory history = new MeasurementHistory();
        history.id = UUID.randomUUID().toString();
        history.timestamp = LocalDateTime.now();
        history.sourceIpAddress = sourceIpAddress;
        history.input = sequence.getValueAsString();
        history.output = formatOutput(output);
        return measurementHistoryRepository.save(history);
    }

    public List<MeasurementHistory> getHistory() {
        return measurementHistoryRepository.findAll();
    }

    public MeasurementHistory getHistoryById(String id) {
        return measurementHistoryRepository.findById(id).orElse(null);
    }
    // PUT uses this method. Only non-null fields are changed.
    public MeasurementHistory updateHistory(String id, MeasurementHistory givenHistory) {
        MeasurementHistory existingHistory = getHistoryById(id);

        if (existingHistory == null) {
            return null;
        }

        if (givenHistory.sourceIpAddress != null) {
            existingHistory.sourceIpAddress = givenHistory.sourceIpAddress;
        }

        if (givenHistory.input != null) {
            existingHistory.input = givenHistory.input;
        }

        if (givenHistory.output != null) {
            existingHistory.output = givenHistory.output;
        }

        return measurementHistoryRepository.save(existingHistory);
    }

    public void clearHistory() {
        measurementHistoryRepository.deleteAll();
    }

    // Converts List<Long> into text before storing it in Oracle DB.
    private String formatOutput(List<Long> output) {
        return output.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));
    }
}
