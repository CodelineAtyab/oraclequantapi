package com.oraclequantapi.oraclequantapi.service;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MeasurementParser {

    public List<Long> parse(String input) {
        List<Long> packageTotals = new ArrayList<>();
        int i = 0;

        while (i < input.length()) {

            // Step 1: read cycleCount
            int cycleCount = 0;
            while (i < input.length()) {
                char c = input.charAt(i);
                i++;
                if (c == 'z') {
                    cycleCount += 26;
                } else {
                    cycleCount += charValue(c);
                    break;
                }
            }

            // Step 2: read values and sum them
            long packageTotal = 0;
            for (int v = 0; v < cycleCount; v++) {
                if (i >= input.length()) break;

                long value = 0;
                while (i < input.length()) {
                    char c = input.charAt(i);
                    i++;
                    if (c == 'z') {
                        value += 26;
                    } else {
                        value += charValue(c);
                        break;
                    }
                }
                packageTotal += value;
            }

            packageTotals.add(packageTotal);
        }

        return packageTotals;
    }

    private int charValue(char c) {
        if (c == '_') return 0;
        return c - 'a' + 1; // a=1, b=2, ..., z=26
    }
}