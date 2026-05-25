package com.oraclequantapi.oraclequantapi.controller;
import jakarta.servlet.http.HttpServletRequest;
import com.oraclequantapi.oraclequantapi.model.MeasurementRecord;
import com.oraclequantapi.oraclequantapi.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/convert-measurements")
public class MeasurementController {

    @Autowired
    public HistoryService historyService;

    @GetMapping
    public ResponseEntity<List<Long>> convertMeasurements(@RequestParam("input") String input,
                                                          HttpServletRequest request) {
        MeasurementRecord record = historyService.save(input, request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.OK).body(record.getOutput());
    }
}
