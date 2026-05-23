package com.oraclequantapi.oraclequantapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// This class represents a database table row for storing conversion history
@Entity
@Table(name = "conversion_history")
public class ConversionHistory {

    // Auto-generated primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Time when the request was made:
    private LocalDateTime timestamp;

    // IP address of the client who made the request
    @Column(name = "source_ip_address")
    private String sourceIpAddress;

    // The input string sent by the client
    @Column(length = 1000)
    private String input;

    // The result returned to the client:
    @Column(length = 2000)
    private String output;

    // Empty constructor required by JPA
    public ConversionHistory() {}

    // Getters and Setters:

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getSourceIpAddress() { return sourceIpAddress; }
    public void setSourceIpAddress(String sourceIpAddress) {
        this.sourceIpAddress = sourceIpAddress;
    }

    public String getInput() { return input; }
    public void setInput(String input) { this.input = input; }

    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }
}