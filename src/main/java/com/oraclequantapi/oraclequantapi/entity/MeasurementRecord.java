package com.oraclequantapi.oraclequantapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MeasurementRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String input;

    private String output;

    public MeasurementRecord() {

    }

    public MeasurementRecord(String input, String output) {
        this.input = input;
        this.output = output;
    }

    public Long getId() {
        return id;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }
}