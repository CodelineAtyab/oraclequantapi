package com.oraclequantapi.oraclequantapi.parser;

public class DecodedNumber {

    private final int value;

    private final int nextIndex;

    public DecodedNumber(int value, int nextIndex) {
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