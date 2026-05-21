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
public class SequenceController {

    private final SequenceService sequenceService;
    private final HistoryService historyService;

    @GetMapping("/convert-measurements")
    public ResponseEntity<List<Integer>> convert(
            @RequestParam("input") String input,
            HttpServletRequest request) {

        log.info("GET /convert-measurements called with input={}", input);

        List<Integer> packages = sequenceService.processSequence(sequenceService.getSequence(input));

        String ip = request.getRemoteAddr();
        historyService.save(input, packages.toString(), ip);

        log.info("Returning response: {}", packages);
        return ResponseEntity.ok(packages);
    }
}
