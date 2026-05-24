package com.oraclequantapi.oraclequantapi.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    private final List<Sequence> sequenceList = new ArrayList<>();

    // Stores enquiry after validating input contains only a-z and underscore
    public Sequence addSequence(Sequence sequence) {
        if (sequence.getInput() == null || !sequence.getInput().matches("^[a-z_]+$")) {
            return null;
        }
        sequenceList.add(sequence);
        return sequence;
    }

    // Returns all stored enquiries as a read-only view
    public List<Sequence> getAllSequences() {
        return Collections.unmodifiableList(sequenceList);
    }

}
