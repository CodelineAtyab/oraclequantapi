package com.oraclequantapi.oraclequantapi.services;

import com.oraclequantapi.oraclequantapi.models.Sequence;
import com.oraclequantapi.oraclequantapi.repositories.SequenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


    @Service
    public class SequenceService {

        private static final Logger logger = LoggerFactory.getLogger(SequenceService.class);

        @Autowired
        private SequenceRepository sequenceRepository;

        public Sequence getSequence(String input) {
            logger.debug("Parsing input into Sequence: {}", input);

            List<String> tokens = new ArrayList<>();
            int[] index = {0};

            while (index[0] < input.length()) {
                int start = index[0];

                // Read the counter character
                char counterChar = input.charAt(index[0]);
                int counter = charToValue(counterChar);
                index[0]++;

                // Advance past exactly `counter` value slots (respecting z-chains)
                for (int v = 0; v < counter; v++) {
                    if (index[0] >= input.length()) break;
                    // consumeOneSlot moves index past the full z-chain for one slot
                    consumeOneSlot(input, index);
                }

                tokens.add(input.substring(start, index[0]));
            }

            Sequence sequence = new Sequence(tokens);
            sequenceRepository.saveSequence(sequence);
            logger.debug("Sequence created with {} package(s)", tokens.size());
            return sequence;
        }


        public List<Integer> processSequence(Sequence sequence) {
            logger.info("Processing sequence: {}", sequence.getValueAsStr());

            List<Integer> results = new ArrayList<>();

            for (String token : sequence.getValue()) {
                int total = decodePackage(token);
                results.add(total);
                logger.debug("Token '{}' → {}", token, total);
            }

            logger.info("Processing result: {}", results);
            return results;
        }


        private int decodePackage(String token) {
            if (token == null || token.isEmpty()) return 0;

            int[] index = {0};

            // First character = counter
            int counter = charToValue(token.charAt(index[0]));
            index[0]++;

            int total = 0;

            for (int v = 0; v < counter; v++) {
                if (index[0] >= token.length()) break;
                total += readOneSlot(token, index);
            }

            return total;
        }

        private int readOneSlot(String input, int[] index) {
            if (index[0] >= input.length()) return 0;

            char c = input.charAt(index[0]);
            index[0]++;

            if (c == 'z') {
                // z chains into the very next character as one slot
                return 26 + readOneSlot(input, index);
            }

            return charToValue(c);
        }


        private void consumeOneSlot(String input, int[] index) {
            if (index[0] >= input.length()) return;

            char c = input.charAt(index[0]);
            index[0]++;

            if (c == 'z') {
                // z chains: consume the next slot too
                consumeOneSlot(input, index);
            }
            // non-z: one character consumed, done
        }

        private int charToValue(char c) {
            if (c == '_') return 0;
            if (c >= 'a' && c <= 'z') return c - 'a' + 1;
            throw new IllegalArgumentException("Invalid character in input: '" + c + "'");
        }
    }

