package com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.repository;

import com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.model.HistoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

 // Data access layer — talks to the database.
 // JpaRepository provides save, findAll, findById and deleteAll with no SQL code.
@Repository
public interface HistoryRepository extends JpaRepository<HistoryRecord, Long> {
    // Long is type of @Id field on HistoryRecord
}
