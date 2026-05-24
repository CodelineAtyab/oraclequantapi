package com.oraclequantapi.oraclequantapi.repository;

import com.oraclequantapi.oraclequantapi.model.HistoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<HistoryRecord, Long> {
}