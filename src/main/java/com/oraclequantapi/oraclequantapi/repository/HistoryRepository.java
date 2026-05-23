package com.oraclequantapi.oraclequantapi.repository;

import com.oraclequantapi.oraclequantapi.model.HistoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
/**
 * Database access layer for HistoryRecord rows.
 *
 * Extending JpaRepository gives this interface ready-made methods such as
 * save, findAll, findById, and deleteAll without writing SQL by hand.
 */
public interface HistoryRepository extends JpaRepository<HistoryRecord, Long> {
}
