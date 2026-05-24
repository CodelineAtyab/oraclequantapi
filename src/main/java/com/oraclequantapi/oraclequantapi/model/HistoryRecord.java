package com.oraclequantapi.oraclequantapi.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "CONVERSION_HISTORY")
public class HistoryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CREATED_AT", nullable = false)
    private Instant timestamp;

}