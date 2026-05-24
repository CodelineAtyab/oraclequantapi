package com.oraclequantapi.oraclequantapi.repositories;


import com.oraclequantapi.oraclequantapi.models.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceRepo extends JpaRepository<Sequence, Long> {
}
