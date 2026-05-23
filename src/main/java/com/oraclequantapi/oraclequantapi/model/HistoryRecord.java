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
/**
 * Database model for one saved conversion request.
 *
 * @Entity tells JPA this class is stored in a database table. Each field below
 * becomes a column in the conversion_history table.
 */
public class HistoryRecord {

    // Primary key. The database sequence creates a new id for each row.
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "history_seq")
    @SequenceGenerator(name = "history_seq", sequenceName = "history_seq", allocationSize = 1)
    private Long id;

    // Date and time when the conversion request was saved.
    @Column(nullable = false)
    private LocalDateTime timestamp;

    // IP address of the client that called the conversion endpoint.
    @Column(name = "source_ip_address", nullable = false)
    private String sourceIpAddress;

    // Original encoded text received from the user.
    @Column(nullable = false)
    private String input;

    // Conversion result stored as text, for example "[2, 6]".
    @Column(nullable = false)
    private String output;
}
