package com.oraclequantapi.oraclequantapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
/**
 * Response shape for returning converted package values.
 *
 * Wraps the package list in a JSON object like {"packages":[2,6]}.
 */
public class ConversionResponse {
    private List<Integer> packages;
}
