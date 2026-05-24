package com.oraclequantapi.oraclequantapi.controller;

import com.oraclequantapi.oraclequantapi.service.HistoryService;
import com.oraclequantapi.oraclequantapi.service.MeasurementService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MeasurementController {
    private static final Logger log = LoggerFactory.getLogger(MeasurementController.class);

    private final MeasurementService measurementService;
    private final HistoryService historyService;

    public MeasurementController(MeasurementService measurementService, HistoryService historyService) {
        this.measurementService = measurementService;
        this.historyService = historyService;
    }

    @GetMapping("/convert-measurements")
    public List<Integer> convert(@RequestParam(required = false) String input, HttpServletRequest request) {

        return List.of();
    }

}