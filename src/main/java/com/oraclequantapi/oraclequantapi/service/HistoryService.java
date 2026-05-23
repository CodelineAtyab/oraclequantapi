package com.oraclequantapi.oraclequantapi.service;
import com.oraclequantapi.oraclequantapi.model.MeasurementRecord;
import com.oraclequantapi.oraclequantapi.repository.MeasurementRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {

    @Autowired
    private MeasurementRecordRepository measurementRecordRepository;

    @Autowired
    private MeasurementParser measurementParser;

    public MeasurementRecord save(String input, String sourceIp) {
        List<Long> output = measurementParser.parse(input);
        MeasurementRecord record = new MeasurementRecord(
                0,
                LocalDateTime.now(),
                sourceIp,
                input,
                output
        );
        return measurementRecordRepository.save(record);
    }

    public List<MeasurementRecord> getAll() {
        return measurementRecordRepository.findAll();
    }

    public Optional<MeasurementRecord> getById(long id) {
        return measurementRecordRepository.findById(id);
    }

    public Optional<MeasurementRecord> update(long id, String newInput) {
        Optional<MeasurementRecord> record = measurementRecordRepository.findById(id);
        record.ifPresent(r -> {
            r.setInput(newInput);
            r.setOutput(measurementParser.parse(newInput));
            measurementRecordRepository.save(r);
        });
        return record;
    }

    public void clearAll() {
        measurementRecordRepository.deleteAll();
    }
}