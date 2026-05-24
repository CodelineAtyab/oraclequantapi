package com.oraclequantapi.oraclequantapi.repository;

import com.oraclequantapi.oraclequantapi.module.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
                "Oracle DB not configured — set datasource URL, username, and password in application.yaml " +
                "and remove the autoconfigure.exclude entries to activate the DB layer."
            );
        }
    }

}
