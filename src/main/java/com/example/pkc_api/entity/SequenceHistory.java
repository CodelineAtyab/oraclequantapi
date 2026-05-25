package com.example.pkc_api.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sequence_history")
@SuppressWarnings("unused")
public class SequenceHistory {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id = UUID.randomUUID();

    private LocalDateTime timestamp;

    @Column(length = 2000)
    private String input;

    @Column(length = 2000)
    private String output;

    public SequenceHistory() {
    }

    public SequenceHistory(LocalDateTime timestamp, String input, String output) {
        this.timestamp = timestamp;
        this.input = input;
        this.output = output;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
