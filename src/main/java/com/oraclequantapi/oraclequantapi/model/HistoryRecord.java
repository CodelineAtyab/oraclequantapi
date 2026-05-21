package com.oraclequantapi.oraclequantapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversion_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "history_seq")
    @SequenceGenerator(name = "history_seq", sequenceName = "history_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "source_ip_address", nullable = false)
    private String sourceIpAddress;

    @Column(nullable = false)
    private String input;

    @Column(nullable = false)
    private String output;
}
