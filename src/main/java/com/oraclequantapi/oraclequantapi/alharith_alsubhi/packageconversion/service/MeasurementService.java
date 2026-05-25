package com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.service;

import java.util.List;


 // Service contract for conversion requests
 // Interface keeps controller decoupled from MeasurementServiceImpl (polymorphism /testing)
 // Run conversion, write logs, and persist one history row

public interface MeasurementService {

    List<Integer> convertAndLog(String input, String sourceIpAddress);
}
