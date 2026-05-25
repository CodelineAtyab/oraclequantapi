package com.oraclequantapi.oraclequantapi.services;

import com.oraclequantapi.oraclequantapi.models.Sequence;
import com.oraclequantapi.oraclequantapi.models.SequenceHistory;
import com.oraclequantapi.oraclequantapi.repositories.SequenceHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SequenceService {
    private static final Logger log = LoggerFactory.getLogger(SequenceService.class);
    private final SequenceHistoryRepository repository;

    public SequenceService(SequenceHistoryRepository repository) {
        this.repository = repository;
    }

    public List<Integer> process_sequence(Sequence sequence, String clientIp) {

        List<Integer> packageTotals = new ArrayList<>();
        List<String> chars = sequence.getValue();

        int index = 0;
        int n = chars.size();

        while (index < n) {

            int packageItemCount = getNextEncodedValue(chars, index);
            index = moveIndexPastValue(chars, index);

            if (packageItemCount == 0) {
                packageTotals.add(0);
                continue;
            }

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

        save_curr_seq(sequence.get_value_as_str(), packageTotals.toString(), clientIp);

        return packageTotals;
    }

    private int getNextEncodedValue(List<String> chars, int startIndex) {
        int sum = 0;
        int i = startIndex;

        while (i < chars.size()) {
            char ch = chars.get(i).charAt(0);
            int val = (ch == '_') ? 0 : (ch - 'a' + 1);
            sum += val;
            i++;

            if (ch != 'z') {
                break;
            }
        }

        return sum;
    }

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
    public void deleteHistoryById(Long id) {
        repository.deleteById(id);
    }

    private void save_curr_seq(String input, String output, String ip) {
        SequenceHistory record = new SequenceHistory(LocalDateTime.now(), ip, input, output);

        repository.save(record);
    }

    public List<SequenceHistory> getAllHistory() {
        return repository.findAll();
    }

    public Optional<SequenceHistory> getHistoryById(Long id) {
        return repository.findById(id);
    }

    public void deleteHistory() {
        repository.deleteAll();
    }

    public SequenceHistory updateHistory(Long id, SequenceHistory updatedDetails) {
        return repository.findById(id).map(record -> {
            record.setInput(updatedDetails.getInput());
            record.setOutput(updatedDetails.getOutput());
            record.setSourceIpAddress(updatedDetails.getSourceIpAddress());
            return repository.save(record);
        }).orElseThrow(() -> new RuntimeException("Record not found: " + id));
    }
}
