package com.oraclequantapi.oraclequantapi.parser;

import java.util.List;

public class MeasurementParser {

    public List<Integer> parseMeasurements(String input) {
        return null;
    }

    private DecodedNumber readEncodedNumber(String input, int currentIndex) {
        char currentCharacter = input.charAt(currentIndex);
        int value = 0;

        if (currentCharacter == 'z') {
            while (currentIndex < input.length()
                    && input.charAt(currentIndex) == 'z') {

                value += 26;
                currentIndex++;
            }

            if (currentIndex < input.length()) {
                char terminatingCharacter = input.charAt(currentIndex);

                if (terminatingCharacter == '_') {
                    value += 0;
                }
                else {
                    value += (terminatingCharacter - 'a') + 1;
                }
                currentIndex++;
            }
        }

        else {
            if (currentCharacter == '_') {
                value = 0;
            } else {
                value = (currentCharacter - 'a') + 1;
            }
            currentIndex++;
        }
        return new DecodedNumber(value, currentIndex);
    }
}
