package com.oraclequantapi.oraclequantapi.controller;

import com.oraclequantapi.oraclequantapi.service.MeasurementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MeasurementController {

    private final MeasurementService measurementService;

    public MeasurementController(
            MeasurementService measurementService
    ) {
        this.measurementService = measurementService;
    }

    @GetMapping("/convert-measurements")
    public List<Integer> convertMeasurements(
            @RequestParam String input
    ) {

        return measurementService.convertMeasurements(input);
    }
}