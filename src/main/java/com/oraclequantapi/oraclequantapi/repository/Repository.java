package com.oraclequantapi.oraclequantapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

//------[DB] Spring Data JPA repository — auto-proxied when JPA auto-config is active
//------[DB] Provides: save, findAll, findById, existsById, deleteById against Oracle SEQUENCE_ENQUIRIES
public interface Repository extends JpaRepository<DATABASE, String> {
}
