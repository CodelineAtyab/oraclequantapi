package com.oraclequantapi.oraclequantapi.repository;

import com.oraclequantapi.oraclequantapi.entity.MeasurementRecord;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementRecordRepository

        extends JpaRepository<MeasurementRecord, Long> {

}
