package om.maryam.measurement.service.impl;

import om.maryam.measurement.dto.HistoryDto;
import om.maryam.measurement.dto.HistoryUpdateRequest;
import om.maryam.measurement.entity.ConversionHistory;
import om.maryam.measurement.exception.HistoryNotFoundException;
import om.maryam.measurement.repository.ConversionHistoryRepository;
import om.maryam.measurement.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CRUD facade over {@link ConversionHistoryRepository}.
 *
 * Entities are never returned outside of this layer; they are translated to
 * {@link HistoryDto} so the persistence model can evolve independently of
 * the REST contract.
 */
@Service
public class HistoryServiceImpl implements HistoryService {

    private static final Logger log = LoggerFactory.getLogger(HistoryServiceImpl.class);

    private final ConversionHistoryRepository repository;

    public HistoryServiceImpl(ConversionHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoryDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HistoryDto findById(Long id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new HistoryNotFoundException(id));
    }

    @Override
    @Transactional
    public HistoryDto update(Long id, HistoryUpdateRequest request) {
        ConversionHistory entity = repository.findById(id)
                .orElseThrow(() -> new HistoryNotFoundException(id));
        entity.setInput(request.getInput());
        entity.setOutput(request.getOutput());
        log.info("Replaced history id={} with new payload", id);
        return toDto(entity);
    }

    @Override
    @Transactional
    public HistoryDto patch(Long id, HistoryUpdateRequest request) {
        ConversionHistory entity = repository.findById(id)
                .orElseThrow(() -> new HistoryNotFoundException(id));
        if (request.getInput() != null && !request.getInput().isBlank()) {
            entity.setInput(request.getInput());
        }
        if (request.getOutput() != null && !request.getOutput().isBlank()) {
            entity.setOutput(request.getOutput());
        }
        log.info("Patched history id={}", id);
        return toDto(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new HistoryNotFoundException(id);
        }
        repository.deleteById(id);
        log.info("Deleted history id={}", id);
    }

    @Override
    @Transactional
    public void deleteAll() {
        long count = repository.count();
        repository.deleteAll();
        log.warn("Cleared history table - {} record(s) removed", count);
    }

    private HistoryDto toDto(ConversionHistory e) {
        return HistoryDto.builder()
                .id(e.getId())
                .timestamp(e.getTimestamp())
                .sourceIpAddress(e.getSourceIpAddress())
                .input(e.getInput())
                .output(e.getOutput())
                .build();
    }
}
