package com.oraclequantapi.oraclequantapi.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Service {

    private final List<Sequence> sequenceList = new ArrayList<>();

    // Stores enquiry and returns it with server-generated id and timestamp
    public Sequence addSequence(Sequence sequence) {
        sequenceList.add(sequence);
        return sequence;
    }

}
