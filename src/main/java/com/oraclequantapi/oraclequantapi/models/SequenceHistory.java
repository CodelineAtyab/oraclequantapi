package com.oraclequantapi.oraclequantapi.models;


import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "MEASUREMENT_HISTORY")

public class SequenceHistory {

    @Id
    @Column(name = "ID", length = 36)
    private String id;

    @Column(name = "TIMESTAMP", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "SOURCE_IP_ADDRESS", length = 64)
    private String sourceIpAddress;

    @Column(name = "INPUT", nullable = false, length = 4000)
    private String input;

    @Column(name = "OUTPUT", length = 4000)
    private String output;

    public SequenceHistory(){
    }

    public SequenceHistory(String id,
                           LocalDateTime timestamp,
                           String sourceIpAddress,
                           String input,
                           String output){
        this.id = id;
        this.timestamp = timestamp;
        this.sourceIpAddress = sourceIpAddress;
        this.input = input;
        this.output = output;
    }

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public LocalDateTime getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp){
        this.timestamp = timestamp;
    }

    public String getSourceIpAddress(){
        return sourceIpAddress;
    }
    public void setSourceIpAddress(String sourceIpAddress){
        this.sourceIpAddress = sourceIpAddress;
    }

    public String getInput(){
        return input;
    }
    public void setInput(String input){
        this.input = input;
    }

    public String getOutput(){
        return output;
    }
    public void setOutput(String output){
        this.output = output;
    }
}
