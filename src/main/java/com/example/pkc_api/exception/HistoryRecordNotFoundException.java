package com.example.pkc_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HistoryRecordNotFoundException extends RuntimeException {

    public HistoryRecordNotFoundException(UUID id) {
        super("History record not found with id: " + id);
    }
}
