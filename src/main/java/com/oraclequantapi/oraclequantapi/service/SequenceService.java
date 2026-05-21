package com.oraclequantapi.oraclequantapi.service;

import com.oraclequantapi.oraclequantapi.model.Sequence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SequenceService {

    public Sequence getSequence(String input) {
        log.info("Creating sequence for input: {}", input);
        return new Sequence(input);
    }

    public List<Integer> processSequence(Sequence sequence) {
        String input = sequence.getInput();
        log.info("Processing sequence: {}", input);
        List<Integer> result = new ArrayList<>();
        int i = 0;
        int len = input.length();

        while (i < len) {
            if (!isLetter(input.charAt(i))) {
                log.info("Non-letter '{}' at position {}, emitting 0", input.charAt(i), i);
                result.add(0);
                i++;
                continue;
            }

            int count = readCount(input, i);
            int countChars = countCharsForCount(input, i);
            i += countChars;

            int total = 0;
            int valuesRead = 0;

            while (valuesRead < count && i < len) {
                char c = input.charAt(i);
                total += (c == '_') ? 0 : valueOfValueChar(c);
                i++;
                valuesRead++;
            }

            if (valuesRead == count) {
                result.add(total);
            }
        }

        log.info("Processed sequence result: {}", result);
        return result;
    }

    private boolean isLetter(char c) {
        return c >= 'a' && c <= 'z';
    }

    private int readCount(String input, int start) {
        int count = 0;
        int i = start;
        while (i < input.length() && input.charAt(i) == 'z') {
            count += 26;
            i++;
        }
        if (i < input.length()) {
            count += charValue(input.charAt(i));
        }
        return count;
    }

    private int countCharsForCount(String input, int start) {
        int i = start;
        while (i < input.length() && input.charAt(i) == 'z') {
            i++;
        }
        if (i < input.length()) {
            i++;
        }
        return i - start;
    }

    private int charValue(char c) {
        return c - 'a' + 1;
    }

    private int valueOfValueChar(char c) {
        if (c == 'z') {
            return 27;
        }
        return c - 'a' + 1;
    }
}
