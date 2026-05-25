package com.oraclequantapi.oraclequantapi.service;

import com.oraclequantapi.oraclequantapi.model.Sequence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
/**
 * Converts encoded letter strings into package totals.
 *
 * Encoding summary:
 * - A letter at the start of a package says how many following characters to read.
 * - a = 1, b = 2, c = 3, and so on.
 * - z is special in the count area: each z adds 26, then the next letter adds more.
 * - In the value area, underscore means 0 and z means 27.
 */
public class SequenceService {

    /**
     * Wraps the raw input string in a Sequence object.
     *
     * This is small today, but it keeps the service method ready if more
     * sequence information is added later.
     */
    public Sequence getSequence(String input) {
        log.info("Creating sequence for input: {}", input);
        return new Sequence(input);
    }

    /**
     * Walks through the encoded text and builds the list of package totals.
     *
     * Example: input "abbcc" returns [2, 6].
     * - "a" means read 1 value: "b" = 2.
     * - "b" means read 2 values: "cc" = 3 + 3 = 6.
     */
    public List<Integer> processSequence(Sequence sequence) {
        String input = sequence.getInput();
        log.info("Processing sequence: {}", input);
        List<Integer> result = new ArrayList<>();
        int i = 0;
        int len = input.length();

        while (i < len) {
            // If a package does not start with a letter, treat that position as value 0.
            if (!isLetter(input.charAt(i))) {
                log.info("Non-letter '{}' at position {}, emitting 0", input.charAt(i), i);
                result.add(0);
                i++;
                continue;
            }

            // First read how many following characters belong to this package.
            int count = readCount(input, i);
            int countChars = countCharsForCount(input, i);
            i += countChars;

            int total = 0;
            int valuesRead = 0;

            // Add the values inside this package until the requested count is reached.
            while (valuesRead < count && i < len) {
                char c = input.charAt(i);
                total += (c == '_') ? 0 : valueOfValueChar(c);
                i++;
                valuesRead++;
            }

            // Only add a package if the input contained enough value characters for it.
            if (valuesRead == count) {
                result.add(total);
            }
        }

        log.info("Processed sequence result: {}", result);
        return result;
    }

    /**
     * This project only treats lowercase a-z as letters in the encoding.
     */
    private boolean isLetter(char c) {
        return c >= 'a' && c <= 'z';
    }

    /**
     * Reads the package size at the current position.
     *
     * A normal letter gives its alphabet number. Leading z characters add 26 each.
     */
    private int readCount(String input, int start) {
        int count = 0;
        int i = start;
        while (i < input.length() && input.charAt(i) == 'z') {
            count += 26;
            i++;
        }
        if (i < input.length() && isLetter(input.charAt(i))) {
            count += charValue(input.charAt(i));
        }
        return count;
    }

    /**
     * Counts how many characters were used to describe the package size.
     *
     * The main loop needs this number so it can skip over the count characters
     * and start reading the package values.
     */
    private int countCharsForCount(String input, int start) {
        int i = start;
        while (i < input.length() && input.charAt(i) == 'z') {
            i++;
        }
        if (i < input.length() && isLetter(input.charAt(i))) {
            i++;
        }
        return i - start;
    }

    /**
     * Converts a count letter into a number: a = 1, b = 2, ... z = 26.
     */
    private int charValue(char c) {
        return c - 'a' + 1;
    }

    /**
     * Converts a package value letter into a number.
     *
     * In value positions, z is treated as 27 by the project rules.
     */
    private int valueOfValueChar(char c) {
        if (c == 'z') {
            return 27;
        }
        return c - 'a' + 1;
    }
}
