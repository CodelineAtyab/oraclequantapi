package com.oraclequantapi.oraclequantapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "history_seq")
    @SequenceGenerator(name = "history_seq", sequenceName = "HISTORY_RECORDS_SEQ", allocationSize = 1)
    private Long id;

    private LocalDateTime timestamp;
    private String sourceIpAddress;

    @Column(length = 4000)
    private String input;

    @Column(length = 4000)
    private String output;
}