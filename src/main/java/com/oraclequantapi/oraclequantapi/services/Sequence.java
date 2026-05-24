package com.oraclequantapi.oraclequantapi.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

// Model representing a single sequence enquiry stored in memory.
// Input field accepts only lowercase a-z and underscore characters.
public class Sequence {

    private String id;

    // Accepted from request body but excluded from all responses
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String input;

    private String currentTime;
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
