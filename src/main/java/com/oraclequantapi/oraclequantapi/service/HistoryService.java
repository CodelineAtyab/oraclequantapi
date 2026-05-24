package com.oraclequantapi.oraclequantapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oraclequantapi.oraclequantapi.model.HistoryRecord;
import com.oraclequantapi.oraclequantapi.repository.HistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HistoryService {
    private final HistoryRepository repository;
    private final ObjectMapper objectMapper;

    public HistoryService(HistoryRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public HistoryRecord record(String input, List<Integer> output, String sourceIpAddress) {

    }

}