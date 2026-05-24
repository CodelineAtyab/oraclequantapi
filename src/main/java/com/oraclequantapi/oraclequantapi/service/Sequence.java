package com.oraclequantapi.oraclequantapi.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

// Model representing a single sequence enquiry stored in memory
public class Sequence {

    private String id;
    private String sequence;
    private String currentTime;

    public Sequence() {
        this.id = UUID.randomUUID().toString();
        this.currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public Sequence(String id, String sequence, String currentTime) {
        this.id = id;
        this.sequence = sequence;
        this.currentTime = currentTime;
    }

    public String getId() {
        return id;
    }

    public String getSequence() {
        return sequence;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
