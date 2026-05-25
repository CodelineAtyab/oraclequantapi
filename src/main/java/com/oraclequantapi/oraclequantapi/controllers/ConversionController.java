package com.oraclequantapi.oraclequantapi.controllers;

import com.oraclequantapi.oraclequantapi.models.Sequence;
import com.oraclequantapi.oraclequantapi.models.SequenceHistory;
import com.oraclequantapi.oraclequantapi.services.HistoryService;
import com.oraclequantapi.oraclequantapi.services.SequenceService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/convert-measurements")
public class ConversionController {

    private static final Logger logger = LoggerFactory.getLogger(ConversionController.class);

   @Autowired
   private SequenceService sequenceService;

   @Autowired
   private HistoryService historyService;

   //endpoint "input"
   @GetMapping
   public ResponseEntity<List<Integer>> convertMeasurements(
           @RequestParam("input") String input,
           HttpServletRequest request) {

       logger.info("GET /measurements?input='{}' | ip='{}'",
               input, request.getRemoteAddr());

       Sequence sequence = sequenceService.getSequence(input);
       List<Integer> result = sequenceService.processSequence(sequence);

       historyService.save(new SequenceHistory(
               null,
               LocalDateTime.now(),
               request.getRemoteAddr(),
               input,
               result.toString()
       ));

       return ResponseEntity.ok(result);
   }
}
