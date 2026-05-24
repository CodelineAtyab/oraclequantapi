package com.oraclequantapi.oraclequantapi.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "CONVERSION_HISTORY")
public class HistoryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CREATED_AT", nullable = false)
    private Instant timestamp;

    @Column(name = "SOURCE_IP_ADDRESS", nullable = false, length = 128)
    @JsonProperty("source_ip_address")
    @JsonAlias("sourceIpAddress")
    private String sourceIpAddress;

    @Lob
    @Column(name = "INPUT")
    private String input;

    @Lob
    @Column(name = "OUTPUT", nullable = false)
    private String output;

    protected HistoryRecord() {
    }

    public HistoryRecord(String sourceIpAddress, String input, String output) {
        this.sourceIpAddress = sourceIpAddress;
        this.input = input;
        this.output = output;
    }

    @PrePersist
    void onCreate() {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
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

}