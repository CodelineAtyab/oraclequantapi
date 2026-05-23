package com.oraclequantapi.oraclequantapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
/**
 * Simple data object that carries the raw input string being converted.
 *
 * Lombok @Data creates getters/setters, and @AllArgsConstructor creates a
 * constructor that accepts the input value.
 */
public class Sequence {
    private String input;
}
