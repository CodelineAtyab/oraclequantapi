package com.example.pkc_api.controller;


import com.example.pkc_api.service.HistoryService;
import com.example.pkc_api.service.MeasurementConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MeasurementController {

    private static final Logger log = LoggerFactory.getLogger(MeasurementController.class);

    private final MeasurementConverter measurementConverter;
    private final HistoryService historyService;

    public MeasurementController(MeasurementConverter measurementConverter, HistoryService historyService) {
        this.measurementConverter = measurementConverter;
        this.historyService = historyService;
    }

    @GetMapping("/convert-measurements")
    public List<Integer> convertMeasurements(
            @RequestParam String input
    ) {
        List<Integer> output = measurementConverter.convertMeasurements(input);

        historyService.saveHistory(input, output.toString());
        log.info("Converted measurement inputLength={} output={}", input.length(), output);

        return output;
    }
}
