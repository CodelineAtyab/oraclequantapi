package com.oraclequantapi.oraclequantapi.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeasurementService {

    public List<Integer> convert(String input) {
        if (input == null) {
            return List.of();
        }

        List<Integer> totals = new ArrayList<>();
        int index = 0;

        while (index < input.length()) {
            ParsedNumber count = readNumber(input, index);
            index = count.nextIndex();

        }
    }
}