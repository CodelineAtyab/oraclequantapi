package com.oraclequantapi.oraclequantapi.services;

import com.oraclequantapi.oraclequantapi.repository.DATABASE;
import com.oraclequantapi.oraclequantapi.repository.Sequence_DATABASE;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Business logic layer — validates inputs, runs the decoder algorithm,
 * and delegates all persistence to Oracle via Sequence_DATABASE.
 */
@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private Sequence_DATABASE sequenceDb;

    //------[DB] Validates input, computes output, then persists the new enquiry
    public DATABASE addSequence(DATABASE db) {
        String input = db.getInput();
        if (input == null || !input.matches("^[a-z_]+$")) {
            return null;
        }
        if (input.charAt(0) == '_') {
            return null;
        }
        db.setOutput(sequenceLogicAlgorithm(input));
        return sequenceDb.persist(db);
    }

    //------[DB] Validates new input, refreshes timestamp and output, then overwrites the record
    public DATABASE updateSequence(DATABASE request) {
        String newInput = request.getInput();
        if (newInput == null || !newInput.matches("^[a-z_]+$") || newInput.charAt(0) == '_') {
            return null;
        }
        request.setCurrentTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        request.setOutput(sequenceLogicAlgorithm(newInput));
        return sequenceDb.update(request);
    }

    //------[DB] Removes the enquiry matching the given id; returns true if deleted, false if not found
    public boolean deleteSequence(String id) {
        return sequenceDb.remove(id);
    }

    //------[DB] Loads all rows from Oracle and recomputes @Transient output from each stored input
    public List<DATABASE> getAllSequences() {
        List<DATABASE> list = sequenceDb.retrieveAll();
        list.forEach(db -> db.setOutput(sequenceLogicAlgorithm(db.getInput())));
        return list;
    }

    // Self-delimiting length-encoded parser:
    //   Header — leading 'z' chars each add 26 to blockLength; the first non-z char adds its value (a=1..z=26, _=0)
    //   Data   — consume exactly blockLength chars, sum their values, append total to output
    private List<Integer> sequenceLogicAlgorithm(String input) {
        List<Integer> output = new ArrayList<>();
        int i = 0;
        while (i < input.length()) {
            int blockLength = 0;
            while (i < input.length() && input.charAt(i) == 'z') {
                blockLength += 26;
                i++;
            }
            if (i < input.length()) {
                char h = input.charAt(i++);
                blockLength += (h == '_') ? 0 : (h - 'a' + 1);
            }
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
