package com.oraclequantapi.oraclequantapi.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service // Marks this class as a Spring service component
public class MeasurementService {

    // Converts encoded input into list of totals
    public List<Integer> convert(String input) {

        // Return empty list if input is null
        if (input == null) {
            return List.of();
        }

        List<Integer> totals = new ArrayList<>(); // Store calculated totals
        int index = 0; // Current reading position in the input string

        // Continue while there are remaining characters
        while (index < input.length()) {
            ParsedNumber count = readNumber(input, index); // Read how many measurements should be processed
            index = count.nextIndex(); // Move to the next unread position
            int total = 0; // Store total for current group

            // Loop through measurements
            for (int valueIndex = 0; valueIndex < count.value(); valueIndex++) {
                // Skip if end of string is reached
                if (index >= input.length()) {
                    continue;
                }

                ParsedNumber measurement = readNumber(input, index); // Read next measurement value
                total += measurement.value(); // Add measurement value to total
                index = measurement.nextIndex(); // Move to next unread position
            }
            totals.add(total); // Save total into result list
        }
        return totals; // Return all totals
    }

    // Reads encoded numbers from the input string
    private ParsedNumber readNumber(String input, int startIndex) {
        char first = input.charAt(startIndex); // Get current character

        // Single character number handling
        if (first != 'z') {
            return new ParsedNumber(symbolValue(first), startIndex + 1);
        }

        // Start reading z-based values
        int index = startIndex;
        int value = 0;

        // Every z adds 26
        while (index < input.length() && input.charAt(index) == 'z') {
            value += 26;
            index++;
        }

        // Read final terminating character
        if (index < input.length()) {
            char terminator = input.charAt(index);
            value += symbolValue(terminator); // Add remaining value
            index++; // Move to next character
        }
        return new ParsedNumber(value, index); // Return parsed value and next position
    }

    // Converts alphabet symbols into numeric values
    private int symbolValue(char symbol) {

        // Underscore means zero
        if (symbol == '_') {
            return 0;
        }

        // Reject invalid characters
        if (symbol < 'a' || symbol > 'z') {
            return 0;
        }
        return symbol - 'a' + 1; // Convert a-z into 1-26
    }

    // Stores parsed number value and next index
    private record ParsedNumber(int value, int nextIndex) {
    }
}