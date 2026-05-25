package com.oraclequantapi.oraclequantapi.repository;

import com.oraclequantapi.oraclequantapi.module.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Oracle persistence wrapper — guards every DB call against an unconfigured datasource.
 * Safe to instantiate without a live DB; failures surface as descriptive RuntimeExceptions.
 */
@Component
public class Sequence_DATABASE {

    //------[DB] Null when datasource auto-config is excluded; checked before every operation
    @Autowired(required = false)
    private Repository repository;

    //------[DB] Throws if the datasource is not configured — called at the top of every method
    private void checkAvailable() {
        if (repository == null) {
            throw new IllegalStateException(
                "Database is not available — contact your administrator to configure the datasource."
            );
        }
    }

    //------[DB] Save a new record to Oracle
    public Sequence persist(Sequence sequence) {
        checkAvailable();
        try {
            return repository.save(sequence);
        } catch (Exception e) {
            throw new RuntimeException("Unable to save your request — a database error occurred.", e);
        }
    }

    //------[DB] Load all records from Oracle
    public List<Sequence> retrieveAll() {
        checkAvailable();
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Unable to retrieve records — a database error occurred.", e);
        }
    }

    //------[DB] Overwrite an existing record; returns null if the id does not exist
    public Sequence update(Sequence sequence) {
        checkAvailable();
        if (!repository.existsById(sequence.getId())) {
            return null;
        }
        try {
            return repository.save(sequence);
        } catch (Exception e) {
            throw new RuntimeException("Unable to update your request — a database error occurred.", e);
        }
    }

    //------[DB] Delete a record by id; returns false if the id does not exist
    public boolean remove(String id) {
        checkAvailable();
        if (!repository.existsById(id)) {
            return false;
        }
        try {
            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Unable to delete the requested record — a database error occurred.", e);
        }
    }

}
