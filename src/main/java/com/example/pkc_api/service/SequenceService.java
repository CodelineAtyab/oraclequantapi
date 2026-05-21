package com.example.pkc_api.service;


import com.example.pkc_api.parser.NumberParser;
import com.example.pkc_api.parser.ParsedNumber;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SequenceService implements MeasurementConverter {

    private final NumberParser numberParser;

    public SequenceService(NumberParser numberParser) {
        this.numberParser = numberParser;
    }

    @Override
    public List<Integer> convertMeasurements(String input) {
        List<Integer> result = new ArrayList<>();

        if (input == null || input.isEmpty()) {
            return result;
        }

        int index = 0;

        while (index < input.length()) {
            ParsedNumber countNumber = numberParser.parse(input, index);

            int count = countNumber.getValue();
            index = countNumber.getNextIndex();

            int packageTotal = 0;

            for (int i = 0; i < count; i++) {
                if (index >= input.length()) {
                    break;
                }

                ParsedNumber measurementNumber = numberParser.parse(input, index);

                packageTotal += measurementNumber.getValue();
                index = measurementNumber.getNextIndex();
            }

            result.add(packageTotal);
        }

        return result;
    }
}
