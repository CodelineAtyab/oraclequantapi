package com.oraclequantapi.oraclequantapi.services;

import com.oraclequantapi.oraclequantapi.module.Sequence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Encapsulates all business logic for sequence enquiries; validates and stores inputs in RAM.
@org.springframework.stereotype.Service
public class Service {

    private final List<Sequence> sequenceList = new ArrayList<>();

    //------[DB] Currently stores in RAM — delegate to Sequence_DATABASE.persist() to switch to Oracle
    // Stores enquiry after validating input contains only a-z and underscore, and does not start with _
    public Sequence addSequence(Sequence sequence) {
        String input = sequence.getInput();
        if (input == null || !input.matches("^[a-z_]+$")) {
            return null;
        }
        if (input.charAt(0) == '_') {
            return null;
        }
        sequence.setOutput(sequenceLogicAlgorithm(input));
        sequenceList.add(sequence);
        return sequence;
    }

    //------[DB] Currently updates in RAM — delegate to Sequence_DATABASE.update() for Oracle persistence
    // Finds enquiry by id, validates new input, re-runs decoder, and updates the stored entry
    public Sequence updateSequence(Sequence request) {
        String newInput = request.getInput();
        if (newInput == null || !newInput.matches("^[a-z_]+$") || newInput.charAt(0) == '_') {
            return null;
        }
        for (Sequence s : sequenceList) {
            if (s.getId().equals(request.getId())) {
                s.setInput(newInput);
                s.setCurrentTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                s.setOutput(sequenceLogicAlgorithm(newInput));
                return s;
            }
        }
        return null;
    }

    //------[DB] Currently deletes from RAM — delegate to Sequence_DATABASE.remove() for Oracle persistence
    // Removes the enquiry matching the given id; returns true if found and deleted
    public boolean deleteSequence(String id) {
        return sequenceList.removeIf(s -> s.getId().equals(id));
    }

    //------[DB] Currently reads from RAM — delegate to Sequence_DATABASE.retrieveAll() for Oracle persistence
    // Returns all stored enquiries as a read-only view
    public List<Sequence> getAllSequences() {
        return Collections.unmodifiableList(sequenceList);
    }

    // Decodes an input string using a self-delimiting length-encoded parser:
    // reads header chars (z=26 each, final non-z char adds its value a=1..z=26, _=0)
    // then sums exactly that many following chars, appending each block total to output.
    private List<Integer> sequenceLogicAlgorithm(String input) {
        List<Integer> output = new ArrayList<>();
        int i = 0;
        while (i < input.length()) {
            // Header phase: consecutive 'z' chars each contribute 26; final non-z char adds its value
            int blockLength = 0;
            while (i < input.length() && input.charAt(i) == 'z') {
                blockLength += 26;
                i++;
            }
            if (i < input.length()) {
                char h = input.charAt(i++);
                blockLength += (h == '_') ? 0 : (h - 'a' + 1);
            }
            // Data phase: consume blockLength chars and sum their values
            int sum = 0;
            for (int j = 0; j < blockLength && i < input.length(); j++, i++) {
                char c = input.charAt(i);
                sum += (c == '_') ? 0 : (c - 'a' + 1);
            }
            output.add(sum);
        }
        return output;
    }

}
