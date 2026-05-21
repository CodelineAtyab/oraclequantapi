package com.oraclequantapi.oraclequantapi.repositories;

import om.oraclequant.pkc_api.models.MeasurementHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementHistoryRepository extends JpaRepository<MeasurementHistory, String> {
    // JpaRepository already provides: findAll, findById, save, deleteById, deleteAll.
}
