package com.oraclequantapi.oraclequantapi.repositories;

import com.oraclequantapi.oraclequantapi.models.OracleQuantRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OracleQuantRepository extends JpaRepository<OracleQuantRecord,Integer> {
}
