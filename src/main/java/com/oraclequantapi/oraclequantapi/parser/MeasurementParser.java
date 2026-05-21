package com.oraclequantapi.oraclequantapi.parser;

import java.util.ArrayList;
import java.util.List;

public class MeasurementParser {

    public List<Integer> parseMeasurements(String input) {
        List<Integer> results = new ArrayList<>();

        int currentIndex = 0;

        while (currentIndex < input.length()) {
            DecodedNumber packageCountResult =
                    readEncodedNumber(input, currentIndex);

            int packageCount = packageCountResult.getValue();

            currentIndex = packageCountResult.getNextIndex();

            int packageTotal = 0;
            int valuesRead = 0;

            while (valuesRead < packageCount
                    && currentIndex < input.length()) {

                DecodedNumber valueResult =
                        readEncodedNumber(input, currentIndex);

                packageTotal += valueResult.getValue();

                currentIndex = valueResult.getNextIndex();

                valuesRead++;
            }
            results.add(packageTotal);
        }
        return results;
    }

    public static void main(String[] args) {

        MeasurementParser parser = new MeasurementParser();

        System.out.println(
                parser.parseMeasurements("za_a_a_a_a_a_a_a_a_a_a_a_a_azaaa")
        );
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
