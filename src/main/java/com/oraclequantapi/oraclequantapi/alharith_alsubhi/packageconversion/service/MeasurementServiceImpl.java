package com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.service;

import com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.model.HistoryRecord;
import com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.repository.HistoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

 // Orchestrates conversion and audit trail.
@Service
public class MeasurementServiceImpl implements MeasurementService {

    private static final Logger logger = LoggerFactory.getLogger(MeasurementServiceImpl.class);

    private final HistoryRepository historyRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public MeasurementServiceImpl(HistoryRepository historyRepository, ObjectMapper objectMapper) {
        this.historyRepository = historyRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Integer> convertAndLog(String input, String sourceIpAddress) {
        // Log incoming request
        logger.info("Incoming measurement conversion request. Source IP: {}, Input: '{}'", sourceIpAddress, input);

        // Run pure conversion logic
        List<Integer> result = ConversionEngine.convert(input);

        // Serialize result as JSON string for the history
        String jsonOutput = "[]";
        try {
            jsonOutput = objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            logger.error("Error serializing conversion result to JSON: {}", e.getMessage(), e);
            jsonOutput = result.toString(); // fallback so API still returns numbers to client
        }

        // Persist history
        try {
            HistoryRecord record = new HistoryRecord(
                    LocalDateTime.now(),
                    sourceIpAddress,
                    input != null ? input : "",
                    jsonOutput
            );
            historyRepository.save(record);
            logger.debug("Successfully saved history record to database for Input: '{}'", input);
        } catch (Exception e) {
            // conversion still succeeds even if DB save fails
            logger.error("Failed to persist history record to database: {}", e.getMessage(), e);
        }

        logger.info("Successfully processed conversion request. Output: {}", jsonOutput);
        return result;
    }
}
