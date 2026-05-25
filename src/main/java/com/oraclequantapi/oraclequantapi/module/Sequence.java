package com.oraclequantapi.oraclequantapi.module;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * JPA entity and domain model for a sequence enquiry.
 * Owns all persistence column mappings and JSON serialization rules.
 */
@Entity
@Table(name = "SEQUENCE_ENQUIRIES")
public class Sequence {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    private String id;

    //------[DB] Write-only in JSON — accepted on requests, excluded from all responses
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "INPUT")
    private String input;

    @Column(name = "CURRENT_TIME")
    private String currentTime;

    //------[DB] Not persisted — recomputed from input on every retrieval
    @Transient
    private List<Integer> output;

    //------[DB] No-arg constructor auto-generates id and timestamp for new records
    public Sequence() {
        this.id = UUID.randomUUID().toString();
        this.currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getId() {
        return id;
    }

    public String getInput() {
        return input;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public List<Integer> getOutput() {
        return output;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public void setOutput(List<Integer> output) {
        this.output = output;
    }
}
