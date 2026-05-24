package com.oraclequantapi.oraclequantapi.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

// Model representing a single sequence enquiry stored in memory
public class Sequence {

    private String id;
    private String input;
    private String currentTime;

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

    public void setId(String id) {
        this.id = id;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
