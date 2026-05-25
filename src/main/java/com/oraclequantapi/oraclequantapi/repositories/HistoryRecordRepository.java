package com.oraclequantapi.oraclequantapi.repositories;

import com.oraclequantapi.oraclequantapi.model.HistoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRecordRepository extends JpaRepository<HistoryRecord, Long> {}
