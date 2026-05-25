package com.example.pkc_api.repository;


import com.example.pkc_api.entity.SequenceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SequenceHistoryRepository extends JpaRepository<SequenceHistory, UUID> {
}
