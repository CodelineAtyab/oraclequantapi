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

//------[DB] JPA entity — maps to SEQUENCE_ENQUIRIES table in Oracle (avoids reserved word SEQUENCE)
// Model representing a single sequence enquiry stored in memory.
// Input field accepts only lowercase a-z and underscore characters.
@Entity
@Table(name = "SEQUENCE_ENQUIRIES")
public class Sequence {

    //------[DB] Primary key — UUID string generated server-side in no-arg constructor
    @Id
    @Column(name = "ID", nullable = false, unique = true)
    private String id;

    // Accepted from request body but excluded from all responses
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "INPUT")
    private String input;

    @Column(name = "CURRENT_TIME")
    private String currentTime;

    //------[DB] Transient field — recomputed from input by algorithm on retrieval, not persisted
    @Transient
    private List<Integer> output;

    public Sequence() {
        this.id = UUID.randomUUID().toString();
        this.currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public Sequence(String id, String input, String currentTime) {
        this.id = id;
        this.input = input;
        this.currentTime = currentTime;
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
