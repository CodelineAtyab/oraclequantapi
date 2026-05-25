package com.oraclequantapi.oraclequantapi.models;

import java.util.ArrayList;
import java.util.List;

public class Sequence {

    private List<String> value;

    public Sequence() {
        this.value = new ArrayList<>();
    }

    public Sequence(List<String> value) {
        this.value = new ArrayList<>(value);
    }

    //UML methods
    public void setValue(List<String> value) {
        this.value = new ArrayList<>(value);
    }

    public String getValueAsStr() {
        return String.join(",", value);
    }

    public boolean isValid() {
        if (value == null || value.isEmpty()) return false;
        for (String token : value) {
            for (char c : token.toCharArray()) {
                if (c != '_' && (c < 'a' || c > 'z'))
                    return false;
            }
        }
        return true;
    }

    public List<String> getValue() {
        return value;
    }
}
