package com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.service;

import com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.model.HistoryRecord;

import java.util.List;
import java.util.Optional;

 // Service contract for history CRUD.
 // HistoryController talks to this interface not the repository directly
public interface HistoryService {

    // All rows
    List<HistoryRecord> getAllRecords();

    // One row by primary key
    Optional<HistoryRecord> getRecordById(Long id);

    // Full update
    HistoryRecord updateRecord(Long id, HistoryRecord updatedRecord);

    // Partial update
    HistoryRecord patchRecord(Long id, HistoryRecord partialRecord);

    // Delete all rows
    void clearHistory();
}
