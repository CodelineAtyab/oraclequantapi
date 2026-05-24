package com.oraclequantapi.oraclequantapi.service;

import com.oraclequantapi.oraclequantapi.model.HistoryRecord;
import com.oraclequantapi.oraclequantapi.repository.HistoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {

    private final HistoryRepository repository;

    public HistoryService(HistoryRepository repository) {
        this.repository = repository;
    }

    //get all
    public List<HistoryRecord> getAll() {
        return repository.findAll();
    }

    //get by id
    public Optional<HistoryRecord> getById(Long id) {
        return repository.findById(id);
    }

    //put / patch - update by ID
    //null fields are skipped (suppoer both full and partial update)

    public HistoryRecord update(Long id, HistoryRecord updated) {
        return repository.findById(id).map(record -> {
            if (updated.getSourceIpAddress() != null)
                record.setSourceIpAddress(updated.getSourceIpAddress());
            if (updated.getInput() != null)
                record.setInput(updated.getInput());
            if (updated.getOutput() != null)
                record.setOutput(updated.getOutput());
            return repository.save(record);
        }).orElseThrow(() ->
                new ResponseStatusException
                        (HttpStatus.NOT_FOUND, "Record not found: " + id));
    }
      //delete all
    public void deleteAll() {
        repository.deleteAll();
    }
}