package com.oraclequantapi.oraclequantapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA interface for SEQUENCE_ENQUIRIES.
 * Auto-proxied at startup when JPA auto-config is active.
 * Provides: save, findAll, findById, existsById, deleteById.
 */
public interface Repository extends JpaRepository<DATABASE, String> {
}
