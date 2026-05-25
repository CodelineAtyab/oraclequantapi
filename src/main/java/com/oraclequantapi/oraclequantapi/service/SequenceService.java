package com.oraclequantapi.oraclequantapi.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.oraclequantapi.oraclequantapi.model.Sequence;

@Service
public class SequenceService {

    public Sequence get_sequence(String str) {
        Sequence sequence = new Sequence();
        if (str != null && !str.isEmpty()) {
            sequence.set_value(Arrays.asList(str.split("")));
        } else {
            sequence.set_value(new ArrayList<>());
        }
        return sequence;
    }

    // Now contains the full decoding algorithm
    public List<Integer> process_sequence(Sequence sequence) {
        List<String> chars = sequence.get_value();
        List<Integer> results = new ArrayList<>();
        int pointer = 0;

        while (pointer < chars.size()) {

            // Step 1: read counter — z chains into next char (26 + next)
            int[] counter = readChained(chars, pointer);
            int counterValue = counter[0];
            pointer          = counter[1];

            // Step 2: read exactly counterValue value-slots, sum them
            int slotSum = 0;
            for (int i = 0; i < counterValue; i++) {
                int[] slot = readChained(chars, pointer);
                slotSum += slot[0];
                pointer  = slot[1];
            }

            results.add(slotSum);
        }

        return results;
    }

    // Handles z-chaining: z + next = 26 + value(next), recursively
    private int[] readChained(List<String> chars, int pos) {
        String ch = chars.get(pos);
        if ("z".equals(ch)) {
            int[] next = readChained(chars, pos + 1);
            return new int[]{ 26 + next[0], next[1] };
        }
        return new int[]{ charValue(ch), pos + 1 };
    }

    // a=1, b=2,.. z=26, _=0
    private int charValue(String ch) {
        if ("_".equals(ch)) return 0;
        return ch.charAt(0) - 'a' + 1;
    }
}
