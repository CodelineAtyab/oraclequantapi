package com.oraclequantapi.oraclequantapi.controller;

import com.oraclequantapi.oraclequantapi.service.ConversionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ConversionController {

    private final ConversionService conversionService;

    public ConversionController(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @GetMapping("/convert-measurements")
    public List<Long> convert(
            @RequestParam String input,
            HttpServletRequest request) {
        return conversionService.convert(input, request.getRemoteAddr());
    }
}