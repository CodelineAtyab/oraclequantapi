package com.oraclequantapi.oraclequantapi.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Column(name = "SOURCE_IP_ADDRESS", nullable = false, length = 128)
    @JsonProperty("source_ip_address")
    @JsonAlias("sourceIpAddress")
    private String sourceIpAddress;

}