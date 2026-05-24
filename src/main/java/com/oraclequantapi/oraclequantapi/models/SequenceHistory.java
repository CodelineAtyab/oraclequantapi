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

    @Column(name = "INPUT_STRING", length = 4000)
    private String input;

    @Column(name = "OUTPUT_STRING", length = 4000)
    private String output;

    public SequenceHistory() {}

    public SequenceHistory(LocalDateTime timestamp, String sourceIpAddress, String input, String output) {
        this.timestamp = timestamp;
        this.sourceIpAddress = sourceIpAddress;
        this.input = input;
        this.output = output;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getSourceIpAddress() { return sourceIpAddress; }
    public void setSourceIpAddress(String sourceIpAddress) { this.sourceIpAddress = sourceIpAddress; }

    public String getInput() { return input; }
    public void setInput(String input) { this.input = input; }

    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }
}
