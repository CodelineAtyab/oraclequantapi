package com.oraclequantapi.oraclequantapi.repository;

import com.oraclequantapi.oraclequantapi.model.MeasurementRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRecordRepository extends JpaRepository<MeasurementRecord, Long> {
}

