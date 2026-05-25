package com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

 // Domain and database entity gives one row per conversion request.
@Entity
@Table(name = "MEASUREMENT_HISTORY")
public class HistoryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // auto-increment primary key

    @Column(name = "TIMESTAMP", nullable = false)
    private LocalDateTime timestamp; // when the request was processed

    @JsonProperty("source_ip_address")
    @Column(name = "SOURCE_IP_ADDRESS", nullable = false)
    private String sourceIpAddress;

    @Column(name = "INPUT_VALUE", length = 4000, nullable = false)
    private String input; // raw query string

    @Column(name = "OUTPUT_VALUE", length = 4000, nullable = false)
    private String output; // JSON array as text

    public HistoryRecord() {
    }

    // Measurement Service Impl when saving a new history row
    public HistoryRecord(LocalDateTime timestamp, String sourceIpAddress, String input, String output) {
        this.timestamp = timestamp;
        this.sourceIpAddress = sourceIpAddress;
        this.input = input;
        this.output = output;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
