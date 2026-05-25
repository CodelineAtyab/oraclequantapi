package com.oraclequantapi.oraclequantapi.repositories;

import com.oraclequantapi.oraclequantapi.models.SequenceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversionHistoryRepository extends JpaRepository<SequenceHistory, String> {
}
