package com.oraclequantapi.oraclequantapi.services;

import com.oraclequantapi.oraclequantapi.repositories.MeasurementHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

    @Autowired
    private MeasurementHistoryRepository measurementHistoryRepository;

}
