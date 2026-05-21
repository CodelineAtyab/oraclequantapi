package com.oraclequantapi.oraclequantapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ConversionResponse {
    private List<Integer> packages;
}