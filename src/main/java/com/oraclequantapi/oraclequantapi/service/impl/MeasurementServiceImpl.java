package com.oraclequantapi.oraclequantapi.service.impl;

import com.oraclequantapi.oraclequantapi.parser.MeasurementParser;
import com.oraclequantapi.oraclequantapi.service.MeasurementService;
import java.util.List;
import org.springframework.stereotype.Service;
import com.oraclequantapi.oraclequantapi.entity.MeasurementRecord;
import com.oraclequantapi.oraclequantapi.repository.MeasurementRecordRepository;
import com.oraclequantapi.oraclequantapi.exception.InvalidMeasurementInputException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MeasurementServiceImpl implements MeasurementService {

    private static final Logger logger =
            LoggerFactory.getLogger(MeasurementServiceImpl.class);

    private final MeasurementRecordRepository
            measurementRecordRepository;

    private final MeasurementParser measurementParser =
            new MeasurementParser();

    public MeasurementServiceImpl(
            MeasurementRecordRepository measurementRecordRepository
    ) {
        this.measurementRecordRepository =
                measurementRecordRepository;
    }

    @Override
    public List<Integer> convertMeasurements(
            String input,
            String sourceIpAddress

    ) {

        logger.info("Received measurement conversion request");

        if (input == null || input.isBlank()) {
            logger.warn("Received invalid blank measurement input");

            throw new InvalidMeasurementInputException(
                    "Input parameter is required"
            );
        }

        List<Integer> results =
                measurementParser.parseMeasurements(input);

        MeasurementRecord measurementRecord =
                new MeasurementRecord(
                        input,
                        results.toString(),
                        sourceIpAddress
                );

        measurementRecordRepository.save(measurementRecord);

        logger.info("Successfully converted measurement input");

        return results;
    }

    @Override
    public List<MeasurementRecord> getMeasurementHistory() {
        logger.info("Fetching measurement history");
        return measurementRecordRepository.findAll();
    }

    @Override
    public MeasurementRecord getMeasurementById(Long id) {

        logger.info("Fetching measurement record by id");

        return measurementRecordRepository.findById(id)
                .orElseThrow(() ->
                        new InvalidMeasurementInputException(
                                "Measurement record not found"
                        )
                );
    }

    @Override
    public void deleteMeasurementRecord(Long id) {

        logger.info("Deleting measurement record");

        if (!measurementRecordRepository.existsById(id)) {

            throw new InvalidMeasurementInputException(
                    "Measurement record not found"
            );
        }

        measurementRecordRepository.deleteById(id);
    }

    @Override
    public MeasurementRecord updateMeasurementRecord(
            Long id,
            MeasurementRecord updatedRecord
    ) {

        logger.info("Updating measurement record");

        MeasurementRecord existingRecord =
                measurementRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new InvalidMeasurementInputException(
                                        "Measurement record not found"
                                )
                        );

        existingRecord.setInput(updatedRecord.getInput());
        existingRecord.setOutput(updatedRecord.getOutput());

        return measurementRecordRepository.save(existingRecord);
    }
}