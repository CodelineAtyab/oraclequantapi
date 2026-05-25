package com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.service;

import java.util.ArrayList;
import java.util.List;

 // Core algorithm — no Spring dependencies (easy to unit test).
 // Two phases for decode string to numbers and group numbers into packages.
public class ConversionEngine {

     // Converts encoded measurement text into package inflow totals.
    public static List<Integer> convert(String input) {
        // Guard is null
        if (input == null || input.trim().isEmpty()) {
            return List.of();
        }

        String cleanInput = input.trim();
        List<Integer> parsedNumbers = new ArrayList<>();
        int i = 0;
        int n = cleanInput.length();

        // Scan left to right
        while (i < n) {
            int currentSum = 0;

            while (i < n && Character.toLowerCase(cleanInput.charAt(i)) == 'z') {
                currentSum += 26;
                i++;
            }

            if (i < n) {
                char ch = Character.toLowerCase(cleanInput.charAt(i));
                int val = 0;
                if (ch == '_') {
                    val = 0;
                } else if (ch >= 'a' && ch <= 'y') {
                    val = ch - 'a' + 1;
                } else {
                    val = 0; // for unknown chars replace 0
                }
                currentSum += val;
                i++;
            }
            parsedNumbers.add(currentSum);
        }

        // Build packages
        List<Integer> packageTotals = new ArrayList<>();
        int index = 0;
        int numCount = parsedNumbers.size();

        while (index < numCount) {
            int count = parsedNumbers.get(index); // package size (how many values to read)
            index++;

            int totalValue = 0;
            for (int k = 0; k < count; k++) {
                if (index < numCount) {
                    totalValue += parsedNumbers.get(index);
                    index++;
                } else {
                    break;
                }
            }
            packageTotals.add(totalValue);
        }

        return packageTotals;
    }
}
