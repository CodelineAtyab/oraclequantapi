package com.oraclequantapi.oraclequantapi.repository;

import com.oraclequantapi.oraclequantapi.model.ConversionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// This interface handles all database operations for ConversionHistory
// JpaRepository automatically provides: save, findAll, findById, deleteAll, etc.
@Repository
public interface HistoryRepository extends JpaRepository<ConversionHistory, Long> {
    // No extra methods needed — JPA covers all required operations
}