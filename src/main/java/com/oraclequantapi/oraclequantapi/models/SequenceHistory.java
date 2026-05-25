package com.oraclequantapi.oraclequantapi.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "SEQUENCE_HISTORY")
public class SequenceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TIMESTAMP", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "SOURCE_IP_ADDRESS", nullable = false)
    private String sourceIpAddress;

    @Lob
    @Column(name = "INPUT_STRING")
    private String input;

    @Lob
    @Column(name = "OUTPUT_STRING")
    private String output;

    public SequenceHistory() {
    }

    public SequenceHistory(LocalDateTime timestamp, String sourceIpAddress, String input, String output) {
        this.timestamp = timestamp;
        this.sourceIpAddress = sourceIpAddress;
        this.input = input;
        this.output = output;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSourceIpAddress() {
        return sourceIpAddress;
    }

    public void setSourceIpAddress(String sourceIpAddress) {
        this.sourceIpAddress = sourceIpAddress;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
