package com.oraclequantapi.oraclequantapi.services;

import com.oraclequantapi.oraclequantapi.models.MeasurementSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeasurementService {

    @Autowired
    private HistoryService historyService;

    // Main method used by the controller: convert the input, then save the request in Oracle DB.
    public List<Long> convertAndSave(String input, String sourceIpAddress) {
        MeasurementSequence sequence = getMeasurement(input);
        List<Long> output = processMeasurement(sequence);
        historyService.saveCurrentMeasurement(sourceIpAddress, sequence, output);
        return output;
    }

    // Build a MeasurementSequence object from the raw query parameter.
    public MeasurementSequence getMeasurement(String givenInput) {
        if (givenInput == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Query parameter 'input' is required.");
        }

        MeasurementSequence sequence = new MeasurementSequence(givenInput);

        if (!sequence.isValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Query parameter 'input' is required.");
        }

        return sequence;
    }

    // Converts the encoded string into package totals.
    public List<Long> processMeasurement(MeasurementSequence sequence) {
        String input = sequence.getValueAsString();
        List<Long> results = new ArrayList<>();
        int index = 0;

        while (index < input.length()) {
            // Every package starts with an encoded count.
            ReadResult countResult = readEncodedNumber(input, index);
            long count = countResult.value;
            index = countResult.nextIndex;

            long packageTotal = 0;
            long valuesRead = 0;

            // Read up to "count" measurement values. If the string ends early, use what exists.
            while (valuesRead < count && index < input.length()) {
                ReadResult measurementResult = readEncodedNumber(input, index);
                packageTotal = packageTotal + measurementResult.value;
                index = measurementResult.nextIndex;
                valuesRead++;
            }

            results.add(packageTotal);
        }

        return results;
    }

    // Reads one encoded number.
    // Examples: "_" = 0, "a" = 1, "z" = 26, "za" = 27, "zza" = 53.
    private ReadResult readEncodedNumber(String input, int startIndex) {
        long value = 0;
        int index = startIndex;

        // Keep adding 26 for every z until the first non-z character appears.
        while (index < input.length() && input.charAt(index) == 'z') {
            value = value + 26;
            index++;
        }

        // If the input ended after z characters, return the total z value.
        if (index >= input.length()) {
            return new ReadResult(value, index);
        }

        char current = input.charAt(index);

        if (current == '_') {
            return new ReadResult(value, index + 1);
        }

        value = value + (current - 'a' + 1);
        return new ReadResult(value, index + 1);
    }

    // Helper object that returns both the decoded value and the next reading position
    private static class ReadResult {
        public long value;
        public int nextIndex;

        public ReadResult(long value, int nextIndex) {
            this.value = value;
            this.nextIndex = nextIndex;
        }
    }

}
