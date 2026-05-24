package com.oraclequantapi.oraclequantapi.controller;

import com.oraclequantapi.oraclequantapi.model.ConversionHistory;
import com.oraclequantapi.oraclequantapi.service.ConversionService;
import com.oraclequantapi.oraclequantapi.service.HistoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

// This controller handles the main conversion endpoint
@RestController
public class ConversionController {

    // Logger for tracking requests and results
    private static final Logger logger = LoggerFactory.getLogger(ConversionController.class);

    private final ConversionService conversionService;
    private final HistoryService historyService;

    // Inject both services via constructor
    public ConversionController(ConversionService conversionService,
                                HistoryService historyService) {
        this.conversionService = conversionService;
        this.historyService = historyService;
    }

    // GET /convert-measurements?input=...
    // Accepts an encoded input string and returns a list of package totals
    @GetMapping("/convert-measurements")
    public ResponseEntity<List<Integer>> convert(
            @RequestParam("input") String input,
            HttpServletRequest request) {

        logger.info("Received conversion request with input: {}", input);

        // Run the conversion algorithm
        List<Integer> result = conversionService.convert(input);

        // Build a history record to persist this request
        ConversionHistory record = new ConversionHistory();
        record.setTimestamp(LocalDateTime.now());               // current time
        record.setSourceIpAddress(request.getRemoteAddr());     // client IP
        record.setInput(input);                                  // original input
        record.setOutput(result.toString());                     // result as string

        // Save the record to the database
        historyService.save(record);

        logger.info("Conversion result: {}", result);

        // Return 200 OK with the result list
        return ResponseEntity.ok(result);
    }
}