package com.oraclequantapi.oraclequantapi.repository;

import com.oraclequantapi.oraclequantapi.module.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

//------[DB] Persistence layer — wraps all Oracle DB operations with exception handling
//------[DB] Safe to instantiate without a live DB; methods fail gracefully with clear messages
@Component
public class Sequence_DATABASE {

    //------[DB] required = false — bean is null when DB auto-config is excluded in application.yaml
    @Autowired(required = false)
    private Repository repository;

    //------[DB] Guard — called before every DB operation to detect unconfigured datasource
    private void checkAvailable() {
        if (repository == null) {
            throw new IllegalStateException(
                "Database is not available — contact your administrator to configure the datasource."
            );
        }
    }

    //------[DB] Persists a new Sequence entity to Oracle via repository.save()
    public Sequence persist(Sequence sequence) {
        checkAvailable();
        try {
            return repository.save(sequence);
        } catch (Exception e) {
            throw new RuntimeException("Unable to save your request — a database error occurred.", e);
        }
    }

    //------[DB] Retrieves all Sequence records from Oracle via repository.findAll()
    public List<Sequence> retrieveAll() {
        checkAvailable();
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Unable to retrieve records — a database error occurred.", e);
        }
    }

    //------[DB] Looks up a single Sequence by UUID primary key via repository.findById()
    public Optional<Sequence> retrieveById(String id) {
        checkAvailable();
        try {
            return repository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Unable to find the requested record — a database error occurred.", e);
        }
    }

    //------[DB] Updates an existing Sequence — checks existsById first, then overwrites via repository.save()
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

    //------[DB] Removes a Sequence by UUID — checks existsById first, then issues repository.deleteById()
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
