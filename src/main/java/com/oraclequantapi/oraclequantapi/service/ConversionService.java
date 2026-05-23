package com.oraclequantapi.oraclequantapi.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

// This service contains the core algorithm for converting the input string
@Service
public class ConversionService {

    // Main method: converts an encoded input string into a list of package totals
    public List<Integer> convert(String input) {
        List<Integer> result = new ArrayList<>();
        int i = 0;

        while (i < input.length()) {
            // Read the counter — tells us how many values are in this package
            int[] counterRead = readNumber(input, i);
            int counter = counterRead[0]; // number of values to read
            i = counterRead[1];           // move index forward

            // Sum up the next 'counter' values
            int sum = 0;
            for (int j = 0; j < counter; j++) {
                if (i >= input.length()) break; // safety check — avoid out of bounds
                int[] valRead = readNumber(input, i);
                sum += valRead[0]; // add value to package total
                i = valRead[1];    // move index forward
            }

            // Add this package's total to the result list
            result.add(sum);
        }

        return result;
    }

    // Reads a single encoded number starting at position 'pos'
    // Rules:
    //   - Each letter maps to a number: a=1, b=2, ..., z=26
    //   - If the letter is 'z', keep reading and adding (multi-char number)
    //   - If the letter is '_', it means 0 and ends the number
    //   - Any other letter ends the number after being added
    private int[] readNumber(String input, int pos) {
        int total = 0;

        while (pos < input.length()) {
            char c = input.charAt(pos);

            if (c == '_') {
                // Underscore means 0 — end of this number
                pos++;
                break;
            }

            // Convert letter to its numeric value (a=1, b=2, ..., z=26)
            int val = c - 'a' + 1;
            total += val;
            pos++;

            if (c != 'z') {
                // Non-z letter ends the number
                break;
            }
            // If c == 'z', continue reading the next character and add it too
        }

        // Return both the number value and the updated index position
        return new int[]{total, pos};
    }
}