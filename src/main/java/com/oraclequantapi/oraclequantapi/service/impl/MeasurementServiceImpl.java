package com.oraclequantapi.oraclequantapi.service.impl;

import com.oraclequantapi.oraclequantapi.parser.MeasurementParser;
import com.oraclequantapi.oraclequantapi.service.MeasurementService;

import java.util.List;

public class MeasurementServiceImpl implements MeasurementService {

    private final MeasurementParser measurementParser =
            new MeasurementParser();

    @Override
    public List<Integer> convertMeasurements(String input) {

        return measurementParser.parseMeasurements(input);
    }
}