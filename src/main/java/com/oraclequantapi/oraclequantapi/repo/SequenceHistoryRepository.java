package com.oraclequantapi.oraclequantapi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.oraclequantapi.oraclequantapi.model.SequenceHistory;

@Repository
public interface SequenceHistoryRepository extends JpaRepository<SequenceHistory, Long> {

}