package com.oraclequantapi.oraclequantapi.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class Converter {

    public Converter() {
    }

    public List<Integer> Convert(String input) {
        String input1 = input.trim().toLowerCase();
        String[] inputArray = input1.split("");
        boolean checked = checkInput(inputArray);
        if (!checked) {
            return null;
        }

        List<Integer> list = valueToInt(inputArray);
        list = backageSize(list);
        return list;
    }

    public boolean checkInput(String[] input) {
        for (String s : input) {
            if (s.isEmpty() || s.matches("^[a-z_]*$")) {
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
            } else if (count < backageCount) {
                size += integer;
                count++;
            } else {
                backageSizeList.add(size);
                count = 0;
                size = 0;
                backageCount = 0;
            }
        }
        return backageSizeList;
    }

    public List<Integer> valueToInt(String[] inputs) {
        List<Integer> valueList = new ArrayList<>();
        int value = 0;

        String[] valid = "_abcdefghijklmnopqrstuvwxyz".split("");

        for (String input : inputs) {
            for (int j = 0; j < valid.length; j++) {
                if (input.equals(valid[j])) {
                    value += j;
                    break;
                }
            }

            if (!input.equals("z")) {
                valueList.add(value);
                value = 0;
            }
        }

        return valueList;
    }
}
