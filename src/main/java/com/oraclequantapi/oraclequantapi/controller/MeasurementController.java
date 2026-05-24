package com.oraclequantapi.oraclequantapi.controller;

import com.oraclequantapi.oraclequantapi.service.HistoryService;
import com.oraclequantapi.oraclequantapi.service.MeasurementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeasurementController {
    private static final Logger log = LoggerFactory.getLogger(MeasurementController.class);

    private final MeasurementService measurementService;
    private final HistoryService historyService;

}