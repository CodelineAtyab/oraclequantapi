package com.oraclequantapi.oraclequantapi.repositories;

import com.oraclequantapi.oraclequantapi.models.MeasurementHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementHistoryRepository extends JpaRepository<MeasurementHistory, String> {
    // JpaRepository already provides: findAll, findById, save, deleteById, deleteAll.
}
