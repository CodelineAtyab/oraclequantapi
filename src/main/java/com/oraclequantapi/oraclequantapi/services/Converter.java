package com.oraclequantapi.oraclequantapi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class Converter {

    private static final Logger log = LoggerFactory.getLogger(Converter.class);

    public Converter() {
    }

    public List<Integer> Convert(String input) {
        log.debug("Starting conversion for input: \"{}\"", input);
        String input1 = input.trim().toLowerCase();
        String[] inputArray = input1.split("");
        boolean checked = checkInput(inputArray);
        if (!checked) {
            log.warn("Invalid input: \"{}\"", input);
            return null;
        }

        List<Integer> list = valueToInt(inputArray);
        log.debug("Value-to-int result: {}", list);

        list = backageSize(list);
        log.debug("Packaging result: {}", list);

        return list;
    }

    public boolean checkInput(String[] input) {
        for (String s : input) {
            if (s.isEmpty() || !s.matches("^[a-z_]*$")) {
                return false;
            }
        }
        return true;
    }

    public List<Integer> backageSize(List<Integer> valuesList) {
        List<Integer> backageSizeList = new ArrayList<>();
        int backageCount = 0;
        int count = 0;
        int size = 0;

        for (Integer integer : valuesList) {
            if (backageCount == 0) {
                backageCount = integer;
                log.trace("Package count: {}", backageCount);
            } else {
                size += integer;
                count++;
                log.trace("Accumulated value: {}, running size: {}, count: {}/{}",
                        integer, size, count, backageCount);
            }
            if (backageCount == count) {
                backageSizeList.add(size);
                log.trace("Package complete: sum={}", size);
                count = 0;
                size = 0;
                backageCount = 0;
            }
        }
        if (count > 0) {
            log.trace("Adding partial package: sum={}", size);
            backageSizeList.add(size);
        }
        return backageSizeList;
    }

    public List<Integer> valueToInt(String[] inputs) {
        List<Integer> valueList = new ArrayList<>();
        int value = 0;

        String[] valid = "_abcdefghijklmnopqrstuvwxyz".split("");

        for (int i =0; i < inputs.length; i++) {
            for (int j = 0; j < valid.length; j++) {
                if (inputs[i].equals(valid[j])) {
                    value += j;
                    break;
                }
            }

            if (!inputs[i].equals("z") || i == inputs.length -1) {
                valueList.add(value);
                log.trace("Decoded char '{}' -> accumulated value: {}", inputs[i], value);
                value = 0;
            } else {
                log.trace("Char 'z' encountered, accumulating (total: {})", value);
            }
        }

        return valueList;
    }
}
