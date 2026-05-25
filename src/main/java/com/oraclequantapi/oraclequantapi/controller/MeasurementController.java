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

@RestController // Marks this class as a REST controller
public class MeasurementController {
    // Logger used for request and response logging
    private static final Logger log = LoggerFactory.getLogger(MeasurementController.class);

    // Service responsible for conversion logic
    private final MeasurementService measurementService;

    // Service responsible for history storage
    private final HistoryService historyService;

    // Constructor dependency injection
    public MeasurementController(MeasurementService measurementService, HistoryService historyService) {
        this.measurementService = measurementService;
        this.historyService = historyService;
    }

    // HTTP GET endpoint for measurement conversion
    @GetMapping("/convert-measurements")
    public List<Integer> convert(@RequestParam(required = false) String input, HttpServletRequest request) {

        // Prevent null input values
        String safeInput;
        if (input == null) {
            safeInput = "";
        }
        else {
            safeInput = input;
        }

        // Log received input
        log.info("Received input: {}", safeInput);

        // Convert encoded input into totals
        List<Integer> output = measurementService.convert(safeInput);

        // Log conversion output
        log.info("Conversion result: {}", output);

        // Save request history into database
        historyService.record(safeInput, output, clientIp(request));

        // Log client IP address
        log.info("Request saved from IP: {}", clientIp(request));

        return output; // Return conversion result
    }

    // Extract client IP address from request
    private String clientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr(); // Fallback to direct remote address
    }
}