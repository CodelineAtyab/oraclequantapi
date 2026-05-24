package com.oraclequantapi.oraclequantapi.models;

import java.util.ArrayList;
import java.util.List;

public class Sequence {

    private final List<String> value;

    public Sequence() {
        this.value = new ArrayList<>();
    }

    public Sequence(String rawInput) {
        this.value = new ArrayList<>();
        set_value(rawInput);
    }

    public void set_value(String rawInput) {
        this.value.clear();
        if (rawInput != null) {
            for (char ch : rawInput.toCharArray()) {
                this.value.add(String.valueOf(ch));
            }
        }
    }

    public String get_value_as_str() {
        return String.join("", this.value);
    }

    public boolean is_valid() {
        if (this.value == null || this.value.isEmpty()) {
            return false;
        }
        for (String s : this.value) {
            char ch = s.charAt(0);
            // Validates that characters are strictly between 'a' and 'z' OR an underscore '_'
            if ((ch < 'a' || ch > 'z') && ch != '_') {
                return false;
            }
        }
        return true;
    }

    public List<String> getValue() {
        return value;
    }
}
