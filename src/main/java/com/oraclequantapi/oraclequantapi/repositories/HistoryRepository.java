package com.oraclequantapi.oraclequantapi.repositories;

import com.oraclequantapi.oraclequantapi.models.HistoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryRecord, Long> {

}