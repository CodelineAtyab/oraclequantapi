package com.oraclequantapi.oraclequantapi.service;

import com.oraclequantapi.oraclequantapi.entity.MeasurementRecord;
import java.util.List;

public interface MeasurementService {
    List<Integer> convertMeasurements(String input);
    List<MeasurementRecord> getMeasurementHistory();
}