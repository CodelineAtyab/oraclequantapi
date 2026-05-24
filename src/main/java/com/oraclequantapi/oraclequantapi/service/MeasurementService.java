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


            int total = 0;
            for (int valueIndex = 0; valueIndex < count.value(); valueIndex++) {
                if (index >= input.length()) {
                    continue;
                }
                ParsedNumber measurement = readNumber(input, index);
                total += measurement.value();
                index = measurement.nextIndex();
            }
            totals.add(total);
        }
        return totals;
    }

    private ParsedNumber readNumber(String input, int startIndex) {
        char first = input.charAt(startIndex);

        if (first != 'z') {
            return new ParsedNumber(symbolValue(first), startIndex + 1);
        }

        int index = startIndex;
        int value = 0;

        while (index < input.length() && input.charAt(index) == 'z') {
            value += 26;
            index++;
        }

        if (index < input.length()) {
            char terminator = input.charAt(index);
            value += symbolValue(terminator);
            index++;
        }
        return new ParsedNumber(value, index);
    }

    private int symbolValue(char symbol) {
        if (symbol == '_') {
            return 0;
        }

    }

}