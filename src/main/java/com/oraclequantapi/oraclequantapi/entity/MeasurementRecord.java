package com.oraclequantapi.oraclequantapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class MeasurementRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    private String sourceIpAddress;

    private String input;

    private String output;

    public MeasurementRecord() {

    }

    public MeasurementRecord(
            String input,
            String output,
            String sourceIpAddress

    ) {
        this.input = input;
        this.output = output;
        this.sourceIpAddress = sourceIpAddress;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getSourceIpAddress() {
        return sourceIpAddress;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}