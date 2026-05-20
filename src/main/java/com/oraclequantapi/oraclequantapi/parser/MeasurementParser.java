package com.oraclequantapi.oraclequantapi.parser;

import java.util.List;

public class MeasurementParser {

    public List<Integer> parseMeasurements(String input) {
    }

    private DecodedNumber readEncodedNumber(String input, int currentIndex) {
        char currentCharacter = input.charAt(currentIndex);
        int value;

        if (currentCharacter == '_') {
            value = 0;
        } else {
            value = (currentCharacter - 'a') + 1;
        }

        return new DecodedNumber(value, currentIndex + 1);
    }

}
