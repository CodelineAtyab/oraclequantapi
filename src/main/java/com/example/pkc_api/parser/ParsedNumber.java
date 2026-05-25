package com.example.pkc_api.parser;

public class ParsedNumber {

    private final int value;
    private final int nextIndex;

    public ParsedNumber(int value, int nextIndex) {
        this.value = value;
        this.nextIndex = nextIndex;
    }

    public int getValue() {
        return value;
    }

    public int getNextIndex() {
        return nextIndex;
    }
}