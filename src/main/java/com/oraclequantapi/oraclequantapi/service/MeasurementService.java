package com.oraclequantapi.oraclequantapi.service;

import com.oraclequantapi.oraclequantapi.entity.MeasurementRecord;
import java.util.List;

public interface MeasurementService {
    List<Integer> convertMeasurements(
            String input,
            String sourceIpAddress
    );

    List<MeasurementRecord> getMeasurementHistory();

    MeasurementRecord getMeasurementById(Long id);

    void deleteMeasurementRecord(Long id);

    MeasurementRecord updateMeasurementRecord(
            Long id,
            MeasurementRecord updatedRecord
    );
}