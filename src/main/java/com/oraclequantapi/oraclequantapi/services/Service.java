package com.oraclequantapi.oraclequantapi.services;

import com.oraclequantapi.oraclequantapi.module.Sequence;
import com.oraclequantapi.oraclequantapi.repository.Sequence_DATABASE;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Encapsulates all business logic for sequence enquiries; validates inputs and delegates persistence to Oracle.
@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private Sequence_DATABASE sequenceDb;

    //------[DB] Delegates to Sequence_DATABASE.persist() — stores validated enquiry in Oracle
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
        return sequenceDb.persist(sequence);
    }

    //------[DB] Delegates to Sequence_DATABASE.update() — validates, refreshes time/output, persists to Oracle
    // Finds enquiry by id, validates new input, re-runs decoder, and updates the stored entry
    public Sequence updateSequence(Sequence request) {
        String newInput = request.getInput();
        if (newInput == null || !newInput.matches("^[a-z_]+$") || newInput.charAt(0) == '_') {
            return null;
        }
        request.setCurrentTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        request.setOutput(sequenceLogicAlgorithm(newInput));
        return sequenceDb.update(request);
    }

    //------[DB] Delegates to Sequence_DATABASE.remove() — removes enquiry by id from Oracle
    // Removes the enquiry matching the given id; returns true if found and deleted
    public boolean deleteSequence(String id) {
        return sequenceDb.remove(id);
    }

    //------[DB] Delegates to Sequence_DATABASE.retrieveAll() — loads all rows from Oracle, recomputes @Transient output
    // Returns all stored enquiries; output is recomputed from input since it is not persisted in Oracle
    public List<Sequence> getAllSequences() {
        List<Sequence> list = sequenceDb.retrieveAll();
        list.forEach(s -> s.setOutput(sequenceLogicAlgorithm(s.getInput())));
        return list;
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
