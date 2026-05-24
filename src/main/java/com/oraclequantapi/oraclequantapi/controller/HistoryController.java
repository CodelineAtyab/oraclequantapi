package com.oraclequantapi.oraclequantapi.controller;

import com.oraclequantapi.oraclequantapi.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/history")
public class HistoryController {
    private static final Logger log = LoggerFactory.getLogger(HistoryController.class);
    private final HistoryService historyService;
}