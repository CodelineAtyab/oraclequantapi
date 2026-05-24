package com.oraclequantapi.oraclequantapi.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeasurementService {

    public List<Integer> convert(String input) {
        if (input == null) {
            return List.of();
        }
    }
}