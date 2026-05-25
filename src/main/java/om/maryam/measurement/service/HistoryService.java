package om.maryam.measurement.service;

import om.maryam.measurement.dto.HistoryDto;
import om.maryam.measurement.dto.HistoryUpdateRequest;

import java.util.List;

/**
 * CRUD operations over the persisted conversion history.
 */
public interface HistoryService {

    List<HistoryDto> findAll();

    HistoryDto findById(Long id);

    HistoryDto update(Long id, HistoryUpdateRequest request);

    HistoryDto patch(Long id, HistoryUpdateRequest request);

    void deleteById(Long id);

    void deleteAll();
}
