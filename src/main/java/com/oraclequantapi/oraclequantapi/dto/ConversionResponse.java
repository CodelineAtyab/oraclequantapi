package com.oraclequantapi.oraclequantapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
/**
 * Response shape for returning converted package values.
 *
 * The current controller returns List<Integer> directly, but this class can be
 * used if the API response later needs a named JSON field like {"packages":[2,6]}.
 */
public class ConversionResponse {
    private List<Integer> packages;
}
