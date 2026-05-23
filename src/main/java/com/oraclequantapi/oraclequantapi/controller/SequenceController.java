package com.oraclequantapi.oraclequantapi.controller;

import com.oraclequantapi.oraclequantapi.service.HistoryService;
import com.oraclequantapi.oraclequantapi.service.SequenceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
/**
 * Web endpoint for converting one encoded input string into package numbers.
 *
 * A controller is the part of a Spring Boot app that receives HTTP requests.
 */
public class SequenceController {

    // Spring injects these services through the constructor created by Lombok.
    private final SequenceService sequenceService;
    private final HistoryService historyService;

    /**
     * Handles: GET /convert-measurements?input=...
     *
     * It reads the input from the URL, converts it, stores a history row, and
     * returns the conversion result as the HTTP response body.
     */
    @GetMapping("/convert-measurements")
    public ResponseEntity<List<Integer>> convert(
            @RequestParam("input") String input,
            HttpServletRequest request) {

        log.info("GET /convert-measurements called with input={}", input);

        // Convert the encoded text into a list of package totals.
        List<Integer> packages = sequenceService.processSequence(sequenceService.getSequence(input));

        // Save what happened so it can be reviewed later from the /history endpoints.
        String ip = request.getRemoteAddr();
        historyService.save(input, packages.toString(), ip);

        log.info("Returning response: {}", packages);
        return ResponseEntity.ok(packages);
    }
}
