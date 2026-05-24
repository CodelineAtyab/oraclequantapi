package com.oraclequantapi.oraclequantapi.repository;

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

//------[DB] JPA entity — maps to SEQUENCE_ENQUIRIES table; owns all persistence and API serialization annotations
//------[DB] UUID and timestamp are auto-generated in the no-arg constructor for new records
@Entity
@Table(name = "SEQUENCE_ENQUIRIES")
public class DATABASE {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    private String id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "INPUT")
    private String input;

    @Column(name = "CURRENT_TIME")
    private String currentTime;

    //------[DB] Transient field — not stored in Oracle; recomputed from input on every retrieval
    @Transient
    private List<Integer> output;

    public DATABASE() {
        this.id = UUID.randomUUID().toString();
        this.currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public DATABASE(String id, String input, String currentTime) {
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
