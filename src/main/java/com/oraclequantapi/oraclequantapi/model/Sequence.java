package com.oraclequantapi.oraclequantapi.model;

import java.util.ArrayList;
import java.util.List;


public class Sequence {

    private List<String> value = new ArrayList<>();

    public Sequence() {}

    public void set_value(List<String> value) {
        this.value = (value != null) ? value : new ArrayList<>();
    }

    public String get_value_as_str() {
        if (value == null || value.isEmpty()) return "";
        return String.join("", value);
    }

    public boolean is_valid() {
        if (value == null || value.isEmpty()) return false;
        for (String element : value) {
            if (element == null || element.length() != 1) return false;
            char ch = element.charAt(0);
            if (!((ch >= 'a' && ch <= 'z') || ch == '_')) return false;
        }
        return true;
    }

    public List<String> get_value() {
        return this.value;
    }
}