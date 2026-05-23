package com.oraclequantapi.oraclequantapi.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "measurement_records")
public class MeasurementRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private LocalDateTime timestamp;
    private String sourceIpAddress;
    private String input;

    @ElementCollection
    @CollectionTable(name = "measurement_output", joinColumns = @JoinColumn(name = "record_id"))
    @OrderColumn(name = "output_index")
    @Column(name = "output_value")
    private List<Long> output;

    public MeasurementRecord() {}

    public MeasurementRecord(long id, LocalDateTime timestamp, String sourceIpAddress, String input, List<Long> output) {
        this.id = id;
        this.timestamp = timestamp;
        this.sourceIpAddress = sourceIpAddress;
        this.input = input;
        this.output = output;
    }

    public long getId() { return id; }

    public LocalDateTime getTimestamp() { return timestamp; }

    public String getSourceIpAddress() { return sourceIpAddress; }

    public String getInput() { return input; }
    public void setInput(String input) { this.input = input; }

    public List<Long> getOutput() { return output; }
    public void setOutput(List<Long> output) { this.output = output; }
}
