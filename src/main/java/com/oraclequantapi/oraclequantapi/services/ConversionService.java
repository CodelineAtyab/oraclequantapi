package com.oraclequantapi.oraclequantapi.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConversionService {

    public List<Integer> convertMeasurements(String input) {

        List<Integer> results = new ArrayList<>();

        int index = 0;

        while (index < input.length()) {

            char counterChar = input.charAt(index);

            int counter = getCharacterValue(counterChar);

            index++;

            int sum = 0;

            for (int i = 0; i < counter && index < input.length(); i++) {

                char currentChar = input.charAt(index);

                sum += getCharacterValue(currentChar);

                while (currentChar == 'z' && index + 1 < input.length()) {

                    index++;

                    currentChar = input.charAt(index);

                    sum += getCharacterValue(currentChar);

                    if (currentChar != 'z') {
                        break;
                    }
                }

                index++;
            }

            results.add(sum);
        }

        return results;
    }

    private int getCharacterValue(char c) {

        if (c == '_') {
            return 0;
        }

        return (c - 'a') + 1;
    }
}