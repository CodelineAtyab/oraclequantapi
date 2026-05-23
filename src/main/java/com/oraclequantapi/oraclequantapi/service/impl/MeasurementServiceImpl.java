package com.oraclequantapi.oraclequantapi.service.impl;

import com.oraclequantapi.oraclequantapi.parser.MeasurementParser;
import com.oraclequantapi.oraclequantapi.service.MeasurementService;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MeasurementServiceImpl implements MeasurementService {

    private final MeasurementParser measurementParser =
            new MeasurementParser();

    @Override
    public List<Integer> convertMeasurements(String input) {

        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(
                    "Input parameter is required"
            );
        }

        return measurementParser.parseMeasurements(input);
    }
}