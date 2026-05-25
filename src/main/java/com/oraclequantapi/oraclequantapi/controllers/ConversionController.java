package com.oraclequantapi.oraclequantapi.controllers;

import com.oraclequantapi.oraclequantapi.services.ConversionService;
import com.oraclequantapi.oraclequantapi.services.HistoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ConversionController {

    private final ConversionService conversionService;
    private final HistoryService historyService;

    public ConversionController(ConversionService conversionService, HistoryService historyService) {
        this.conversionService = conversionService;
        this.historyService = historyService;
    }

    @GetMapping("/convert-measurements")
    public List<Integer> convertMeasurements(@RequestParam String input, HttpServletRequest request) {

        List<Integer> result = conversionService.convertMeasurements(input);

        historyService.saveHistory(input, result.toString(), request.getRemoteAddr());

        return result;
    }
}