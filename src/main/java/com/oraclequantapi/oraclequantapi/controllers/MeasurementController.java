package com.oraclequantapi.oraclequantapi.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MeasurementController {
    @Autowired
    public MeasurementService measurementService;

    // /convert-measurements?input=aa
    @GetMapping(path = "/convert-measurements")
    public ResponseEntity<List<Long>> convertMeasurements(
            @RequestParam(name = "input", required = false) String input,
            @RequestParam(name = "convert-measurements", required = false) String legacyInput,
            HttpServletRequest request
    ) {

        String selectedInput = input != null ? input : legacyInput;
        List<Long> output = measurementService.convertAndSave(selectedInput, getSourceIpAddress(request));
        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    // Get the real client IP when the app is behind a proxy; otherwise use remote address.
    private String getSourceIpAddress(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");

        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}
