package com.oraclequantapi.oraclequantapi.services;

import com.oraclequantapi.oraclequantapi.models.Sequence;
import com.oraclequantapi.oraclequantapi.models.SequenceHistory;
import com.oraclequantapi.oraclequantapi.repositories.SequenceHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public class SequenceService {

    private static final Logger log = LoggerFactory.getLogger(SequenceService.class);
    private final SequenceHistoryRepository repository;

    // Constructor Dependency Injection Restored
    public SequenceService(SequenceHistoryRepository repository) {
        this.repository = repository;
    }

    /**
     * Core business logic method that processes the character sequence stream,
     * calculates package measurements, and automatically records transaction metrics to the DB.
     */
    public List<Integer> process_sequence(Sequence sequence, String clientIp) {
        log.info("Processing sequence stream for input values: {}", sequence.get_value_as_str());

        List<Integer> packageTotals = new ArrayList<>();
        List<String> chars = sequence.getValue();
        int index = 0;
        int n = chars.size();

        while (index < n) {
            // 1. Calculate the Package Item Count constraint
            int packageItemCount = getNextEncodedValue(chars, index);
            index = moveIndexPastValue(chars, index);

            // Handle edge case where item count resolves to 0 (e.g., input="a_")
            if (packageItemCount == 0) {
                packageTotals.add(0);
                continue;
            }

            // 2. Sum up 'packageItemCount' groups of individual values
            int currentPackageSum = 0;
            for (int i = 0; i < packageItemCount; i++) {
                if (index < n) {
                    int value = getNextEncodedValue(chars, index);
                    currentPackageSum += value;
                    index = moveIndexPastValue(chars, index);
                }
            }

            packageTotals.add(currentPackageSum);
        }

        // Restored: Persist operational trace records into Oracle XE DB
        save_curr_seq(sequence.get_value_as_str(), packageTotals.toString(), clientIp);

        return packageTotals;
    }

    /**
     * Helper method to look ahead and sum character weights up to the first non-'z' token.
     */
    private int getNextEncodedValue(List<String> chars, int startIndex) {
        int sum = 0;
        int i = startIndex;
        while (i < chars.size()) {
            char ch = chars.get(i).charAt(0);
            int val = (ch == '_') ? 0 : (ch - 'a' + 1);
            sum += val;
            i++;
            if (ch != 'z') {
                break; // Terminate accumulation grouping
            }
        }
        return sum;
    }

    /**
     * Helper method to progress the main stream pointer past a processed value block.
     */
    private int moveIndexPastValue(List<String> chars, int startIndex) {
        int i = startIndex;
        while (i < chars.size()) {
            char ch = chars.get(i).charAt(0);
            i++;
            if (ch != 'z') {
                break;
            }
        }
        return i;
    }

    /**
     * Internal persistence call to write history logs.
     */
    private void save_curr_seq(String input, String output, String ip) {
        SequenceHistory record = new SequenceHistory(LocalDateTime.now(), ip, input, output);
        repository.save(record);
        log.info("Database trace logged successfully to Oracle XE.");
    }

    // =========================================================================
    // RESTORED CRUD OPERATIONS FOR API COMPLETENESS
    // =========================================================================

    /**
     * Fetches all rows from the SEQUENCE_HISTORY table.
     */
    public List<SequenceHistory> getAllHistory() {
        return repository.findAll();
    }

    /**
     * Fetches a specific row by its Primary Key ID.
     */
    public Optional<SequenceHistory> getHistoryById(Long id) {
        return repository.findById(id);
    }

    /**
     * Clears out all records in the log table.
     */
    public void deleteHistory() {
        repository.deleteAll();
        log.warn("Sequence application data history completely purged from Oracle XE.");
    }

    /**
     * Updates an existing history item in the database.
     */
    public SequenceHistory updateHistory(Long id, SequenceHistory updatedDetails) {
        return repository.findById(id).map(record -> {
            record.setInput(updatedDetails.getInput());
            record.setOutput(updatedDetails.getOutput());
            record.setSourceIpAddress(updatedDetails.getSourceIpAddress());
            return repository.save(record);
        }).orElseThrow(() -> new RuntimeException("Record index matching ID reference not found: " + id));
    }
}