package com.oraclequantapi.oraclequantapi.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
    public DATABASE persist(DATABASE db) {
        checkAvailable();
        try {
            return repository.save(db);
        } catch (Exception e) {
            throw new RuntimeException("Unable to save your request — a database error occurred.", e);
        }
    }

    //------[DB] Load all records from Oracle
    public List<DATABASE> retrieveAll() {
        checkAvailable();
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Unable to retrieve records — a database error occurred.", e);
        }
    }

    //------[DB] Find a single record by UUID primary key
    public Optional<DATABASE> retrieveById(String id) {
        checkAvailable();
        try {
            return repository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Unable to find the requested record — a database error occurred.", e);
        }
    }

    //------[DB] Overwrite an existing record; returns null if the id does not exist
    public DATABASE update(DATABASE db) {
        checkAvailable();
        if (!repository.existsById(db.getId())) {
            return null;
        }
        try {
            return repository.save(db);
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
