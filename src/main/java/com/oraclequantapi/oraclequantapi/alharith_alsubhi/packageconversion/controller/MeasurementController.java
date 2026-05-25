package com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.controller;

import com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.service.MeasurementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

 // Web layer exposes the conversion REST endpoint.
@RestController
public class MeasurementController {

    private final MeasurementService measurementService;

    @Autowired
    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

     // Returns JSON array of package totals
    @GetMapping("/convert-measurements")
    public ResponseEntity<List<Integer>> convertMeasurements(
            @RequestParam(name = "input", required = false) String input,
            @RequestParam(name = "convert-measurements", required = false) String convertMeasurements,
            HttpServletRequest request) {

        // Read query string
        String targetInput = input != null ? input : convertMeasurements;
        if (targetInput == null) {
            targetInput = ""; // Hint: empty input → empty result list, not an error.
        }

        // Capture client IP for history
        String clientIp = request.getRemoteAddr();
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            clientIp = xForwardedFor.split(",")[0].trim();
        }

        // Delegate: convert, log, and save row in database.
        List<Integer> result = measurementService.convertAndLog(targetInput, clientIp);

        // Return 200 OK
        return ResponseEntity.ok(result);
    }
}
