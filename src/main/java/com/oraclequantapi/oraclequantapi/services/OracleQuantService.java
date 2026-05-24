package com.oraclequantapi.oraclequantapi.services;

import com.oraclequantapi.oraclequantapi.models.OracleQuantRecord;
import com.oraclequantapi.oraclequantapi.repositories.OracleQuantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OracleQuantService {

    private static final Logger log = LoggerFactory.getLogger(OracleQuantService.class);

    private final Converter converter;
    private final OracleQuantRepository repository;

    public OracleQuantService(Converter converter, OracleQuantRepository repository) {
        this.converter = converter;
        this.repository = repository;
    }

    public List<Integer> convertMeasurements(String input, String sourceIp) {
        log.debug("Converting measurement - input: \"{}\", source IP: {}", input, sourceIp);
        List<Integer> result = converter.Convert(input);

        OracleQuantRecord record = new OracleQuantRecord();
        record.setTimestamp(LocalDateTime.now());
        record.setSourceIpAddress(sourceIp);
        record.setInput(input);
        record.setOutput(result != null ? result.toString() : null);
        repository.save(record);
        log.debug("Saved conversion history record for input: \"{}\", output: {}", input, result);

        return result;
    }

    public List<OracleQuantRecord> getAllHistory() {
        log.debug("Fetching all history records");
        return repository.findAll();
    }

    public Optional<OracleQuantRecord> getHistoryById(Integer id) {
        log.debug("Fetching history record by id: {}", id);
        return repository.findById(id);
    }

    public OracleQuantRecord updateHistory(Integer id, OracleQuantRecord updated) {
        log.debug("Updating history record: {}", id);
        OracleQuantRecord record = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Record not found for update: {}", id);
                    return new RuntimeException("Record not found: " + id);
                });
        record.setInput(updated.getInput());
        record.setOutput(updated.getOutput());
        record.setSourceIpAddress(updated.getSourceIpAddress());
        OracleQuantRecord saved = repository.save(record);
        log.debug("Updated history record: {}", id);
        return saved;
    }

    public void clearHistory() {
        log.debug("Clearing all history records");
        repository.deleteAll();
        log.debug("All history records cleared");
    }

    public void deleteHistoryById(Integer id) {
        log.debug("Deleting history record: {}", id);
        repository.deleteById(id);
        log.debug("Deleted history record: {}", id);
    }
}
