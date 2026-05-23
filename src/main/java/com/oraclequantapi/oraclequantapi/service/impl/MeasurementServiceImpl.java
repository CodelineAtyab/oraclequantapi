package com.oraclequantapi.oraclequantapi.service.impl;

import com.oraclequantapi.oraclequantapi.parser.MeasurementParser;
import com.oraclequantapi.oraclequantapi.service.MeasurementService;
import java.util.List;
import org.springframework.stereotype.Service;
import com.oraclequantapi.oraclequantapi.entity.MeasurementRecord;
import com.oraclequantapi.oraclequantapi.repository.MeasurementRecordRepository;

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
    public List<Integer> convertMeasurements(String input) {

        logger.info("Received measurement conversion request");

        if (input == null || input.isBlank()) {
            logger.warn("Received invalid blank measurement input");

            throw new IllegalArgumentException(
                    "Input parameter is required"
            );
        }

        logger.info("Successfully converted measurement input");

        return measurementParser.parseMeasurements(input);
    }
}