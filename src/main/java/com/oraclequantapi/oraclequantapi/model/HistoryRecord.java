package com.oraclequantapi.oraclequantapi.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.Instant;

@Entity // Marks this class as a database entity
@Table(name = "CONVERSION_HISTORY") // Maps this entity to the CONVERSION_HISTORY table
public class HistoryRecord {

    @Id // Primary key of the table
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto generate ID values

    @Column(name = "ID") // Maps the field to the ID column
    private Long id;

    @Column(name = "CREATED_AT", nullable = false) // Stores the record creation timestamp

    private Instant timestamp;

    @Column(name = "SOURCE_IP_ADDRESS", nullable = false, length = 128) // Stores the source IP address

    @JsonProperty("source_ip_address") // JSON property name when sending response

    @JsonAlias("sourceIpAddress") // Accept alternative JSON field name
    private String sourceIpAddress;

    // Stores request input text. Empty Oracle strings are treated as null by the database.
    @Column(name = "INPUT", length = 4000)
    private String input;

    // Stores generated output JSON.
    @Column(name = "OUTPUT", nullable = false, length = 4000)
    private String output;

    // Default constructor required by JPA
    protected HistoryRecord() {
    }

    // Constructor used to create a new history record
    public HistoryRecord(String sourceIpAddress, String input, String output) {
        this.sourceIpAddress = sourceIpAddress;
        this.input = input;
        this.output = output;
    }

    // Automatically runs before saving the entity
    @PrePersist
    void onCreate() {
        // Set current timestamp if it is empty
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }

    // Returns the database ID
    public Long getId() {
        return id;
    }

    // Returns the creation timestamp
    public Instant getTimestamp() {
        return timestamp;
    }

    // Updates the timestamp
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    // Returns the source IP address
    public String getSourceIpAddress() {
        return sourceIpAddress;
    }

    // Updates the source IP address
    public void setSourceIpAddress(String sourceIpAddress) {
        this.sourceIpAddress = sourceIpAddress;
    }

    // Returns the input value
    public String getInput() {
        return input;
    }

    // Updates the input value
    public void setInput(String input) {
        this.input = input;
    }

    // Returns the output value
    public String getOutput() {
        return output;
    }

    // Updates the output value
    public void setOutput(String output) {
        this.output = output;
    }
}