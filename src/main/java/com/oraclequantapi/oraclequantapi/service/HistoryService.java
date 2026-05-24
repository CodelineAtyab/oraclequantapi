package com.oraclequantapi.oraclequantapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oraclequantapi.oraclequantapi.repository.HistoryRepository;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {
    private final HistoryRepository repository;
    private final ObjectMapper objectMapper;
}