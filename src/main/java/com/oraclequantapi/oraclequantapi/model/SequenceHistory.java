package com.oraclequantapi.oraclequantapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SEQUENCE_HISTORY")
public class SequenceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_hist_gen")
    @SequenceGenerator(name = "seq_hist_gen", sequenceName = "SEQ_HIST_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TIMESTAMP_COL")
    private LocalDateTime timestamp;

    @Column(name = "SOURCE_IP", length = 45)
    private String sourceIpAddress;

    @Column(name = "INPUT_COL", length = 4000)
    private String inputColumn;

    @Column(name = "OUTPUT_COL", length = 4000)
    private String outputColumn;

    @Transient
    private List<Sequence> list_of_seq = new ArrayList<>();

    protected SequenceHistory() {}

    public boolean save_curr_seq(Sequence sequence) {
        if (sequence == null) return false;
        this.list_of_seq.add(sequence);
        this.timestamp   = LocalDateTime.now();
        this.inputColumn = sequence.get_value_as_str();
        return true;
    }

    public List<Sequence> get_history() { return this.list_of_seq; }

    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }
    public LocalDateTime getTimestamp()        { return timestamp; }
    public void setTimestamp(LocalDateTime t)  { this.timestamp = t; }
    public String getSourceIpAddress()         { return sourceIpAddress; }
    public void setSourceIpAddress(String ip)  { this.sourceIpAddress = ip; }
    public String getInputColumn()             { return inputColumn; }
    public void setInputColumn(String s)       { this.inputColumn = s; }
    public String getOutputColumn()            { return outputColumn; }
    public void setOutputColumn(String s)      { this.outputColumn = s; }
}