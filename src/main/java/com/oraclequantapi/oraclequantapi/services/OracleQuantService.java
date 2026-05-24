package com.oraclequantapi.oraclequantapi.services;

import com.oraclequantapi.oraclequantapi.models.OracleQuantRecord;
import com.oraclequantapi.oraclequantapi.repositories.OracleQuantRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OracleQuantService {

    private final Converter converter;
    private final OracleQuantRepository repository;

    public OracleQuantService(Converter converter, OracleQuantRepository repository) {
        this.converter = converter;
        this.repository = repository;
    }

    public List<Integer> convertMeasurements(String input, String sourceIp) {
        List<Integer> result = converter.Convert(input);

        OracleQuantRecord record = new OracleQuantRecord();
        record.setTimestamp(LocalDateTime.now());
        record.setSourceIpAddress(sourceIp);
        record.setInput(input);
        record.setOutput(result != null ? result.toString() : null);
        repository.save(record);

        return result;
    }

    public List<OracleQuantRecord> getAllHistory() {
        return repository.findAll();
    }

    public Optional<OracleQuantRecord> getHistoryById(Integer id) {
        return repository.findById(id);
    }

    public OracleQuantRecord updateHistory(Integer id, OracleQuantRecord updated) {
        OracleQuantRecord record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found: " + id));
        record.setInput(updated.getInput());
        record.setOutput(updated.getOutput());
        record.setSourceIpAddress(updated.getSourceIpAddress());
        return repository.save(record);
    }

    public void clearHistory() {
        repository.deleteAll();
    }

    public void deleteHistoryById(Integer id) {
        repository.deleteById(id);
    }
}
