package com.oraclequantapi.oraclequantapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "CONVERSION_HISTORY")
public class HistoryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
}