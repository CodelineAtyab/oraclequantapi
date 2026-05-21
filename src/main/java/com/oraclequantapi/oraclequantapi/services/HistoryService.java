package com.oraclequantapi.oraclequantapi.services;

import com.oraclequantapi.oraclequantapi.repositories.MeasurementHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
}
