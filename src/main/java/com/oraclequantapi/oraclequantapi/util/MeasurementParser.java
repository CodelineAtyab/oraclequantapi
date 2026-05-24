package com.oraclequantapi.oraclequantapi.util;

import java.util.ArrayList;
import java.util.List;

public final class MeasurementParser {

    private MeasurementParser() {}

    public static List<Long> parse(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input must not be null or empty.");
        }

        List<Long> results = new ArrayList<>();
        int i = 0;

        while (i < input.length()) {

            long count = 0;
            while (i < input.length()) {
                char c = input.charAt(i++);
                count += charValue(c);
                if (c != 'z') break;
            }

            long sum = 0;
            for (int j = 0; j < count && i < input.length(); j++) {
                long val = 0;
                while (i < input.length()) {
                    char c = input.charAt(i++);
                    val += charValue(c);
                    if (c != 'z') break;
                }
                sum += val;
            }

            results.add(sum);
        }

        return results;
    }

    public static long charValue(char c) {
        return c == '_' ? 0L : (long)(c - 'a' + 1);
    }
}
