package om.maryam.measurement.service.impl;

import om.maryam.measurement.algorithm.MeasurementDecoder;
import om.maryam.measurement.entity.ConversionHistory;
import om.maryam.measurement.repository.ConversionHistoryRepository;
import om.maryam.measurement.service.MeasurementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Default {@link MeasurementService} implementation.
 *
 * Responsibilities:
 *   1. Delegate the algorithm to {@link MeasurementDecoder} (single
 *      responsibility - no string parsing leaks into the service).
 *   2. Persist a fully populated audit row inside a transaction.
 */
@Service
public class MeasurementServiceImpl implements MeasurementService {

    private static final Logger log = LoggerFactory.getLogger(MeasurementServiceImpl.class);

    private final MeasurementDecoder decoder;
    private final ConversionHistoryRepository historyRepository;

    public MeasurementServiceImpl(MeasurementDecoder decoder,
                                  ConversionHistoryRepository historyRepository) {
        this.decoder = decoder;
        this.historyRepository = historyRepository;
    }

    @Override
    @Transactional
    public List<Integer> convertAndRecord(String encoded, String sourceIp) {
        log.debug("Decoding measurement payload (length={}) from ip={}",
                encoded == null ? 0 : encoded.length(), sourceIp);

        final List<Integer> result = decoder.decode(encoded);

        final ConversionHistory entity = ConversionHistory.builder()
                .timestamp(LocalDateTime.now())
                .sourceIpAddress(sourceIp)
                .input(encoded == null ? "" : encoded)
                .output(result.toString())
                .build();
        historyRepository.save(entity);

        log.info("Decoded input='{}' -> {} (id={})", encoded, result, entity.getId());
        return result;
    }
}
