package om.maryam.measurement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import om.maryam.measurement.dto.HistoryDto;
import om.maryam.measurement.dto.HistoryUpdateRequest;
import om.maryam.measurement.service.HistoryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD endpoints over the persisted conversion history.
 * Base path: <code>/history</code>.
 */
@RestController
@RequestMapping(path = "/history",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Conversion History")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Operation(summary = "Return every history record currently stored.")
    @GetMapping
    public ResponseEntity<List<HistoryDto>> findAll() {
        return ResponseEntity.ok(historyService.findAll());
    }

    @Operation(summary = "Return a single history record by id.")
    @GetMapping("/{id}")
    public ResponseEntity<HistoryDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(historyService.findById(id));
    }

    @Operation(summary = "Replace an existing history record.")
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HistoryDto> update(@PathVariable Long id,
                                             @Valid @RequestBody HistoryUpdateRequest request) {
        return ResponseEntity.ok(historyService.update(id, request));
    }

    @Operation(summary = "Partially update an existing history record.")
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HistoryDto> patch(@PathVariable Long id,
                                            @RequestBody HistoryUpdateRequest request) {
        return ResponseEntity.ok(historyService.patch(id, request));
    }

    @Operation(summary = "Delete a single history record by id.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable Long id) {
        historyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Clear the entire history table.")
    @DeleteMapping
    public ResponseEntity<Void> clearAll() {
        historyService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
