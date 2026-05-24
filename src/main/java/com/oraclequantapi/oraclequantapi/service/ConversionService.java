package com.oraclequantapi.oraclequantapi.service;

import com.oraclequantapi.oraclequantapi.model.HistoryRecord;
import com.oraclequantapi.oraclequantapi.repository.HistoryRepository;
import com.oraclequantapi.oraclequantapi.util.MeasurementParser;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConversionService {

    private final HistoryRepository historyRepository;

    public ConversionService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<Long> convert(String input, String sourceIp) {

        // Step 1 — run the algorithm
        List<Long> result = MeasurementParser.parse(input);

        // Step 2 — save every request to Oracle XE
        HistoryRecord record = new HistoryRecord();
        record.setTimestamp(LocalDateTime.now());
        record.setSourceIpAddress(sourceIp);
        record.setInput(input);
        record.setOutput(result.toString());
        historyRepository.save(record);

        return result;
    }
}