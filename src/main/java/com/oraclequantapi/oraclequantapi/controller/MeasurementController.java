package com.oraclequantapi.oraclequantapi.controller;

import com.oraclequantapi.oraclequantapi.service.MeasurementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.oraclequantapi.oraclequantapi.entity.MeasurementRecord;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
public class MeasurementController {

    private final MeasurementService measurementService;

    public MeasurementController(
            MeasurementService measurementService
    ) {
        this.measurementService = measurementService;
    }

    @GetMapping("/convert-measurements")
    public List<Integer> convertMeasurements(
            @RequestParam String input,
            HttpServletRequest request

    ) {

        return measurementService.convertMeasurements(
                input,
                request.getRemoteAddr()
        );
    }

    @GetMapping("/measurement-history")
    public List<MeasurementRecord> getMeasurementHistory() {

        return measurementService.getMeasurementHistory();

    }

    @GetMapping("/measurement-history/{id}")
    public MeasurementRecord getMeasurementById(
            @PathVariable Long id
    ) {
        return measurementService.getMeasurementById(id);
    }

    @PutMapping("/measurement-history/{id}")
    public MeasurementRecord updateMeasurementRecord(
            @PathVariable Long id,
            @RequestBody MeasurementRecord updatedRecord

    ) {
        return measurementService.updateMeasurementRecord(
                id,
                updatedRecord
        );
    }

    @DeleteMapping("/measurement-history/{id}")
    public void deleteMeasurementRecord(
            @PathVariable Long id
) {

            measurementService.deleteMeasurementRecord(id);
        }
}