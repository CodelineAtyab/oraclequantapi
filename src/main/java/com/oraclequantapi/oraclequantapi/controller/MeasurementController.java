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

        String safeInput;
        if (input == null) {
            safeInput = "";
        }
        else {
            safeInput = input;
        }

        log.info("Received input: {}", safeInput);

        List<Integer> output = measurementService.convert(safeInput);

        log.info("Conversion result: {}", output);

        historyService.record(safeInput, output, clientIp(request));

        log.info("Request saved from IP: {}", clientIp(request));

        return output;
    }

    private String clientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

}