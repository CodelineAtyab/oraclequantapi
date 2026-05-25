package om.maryam.measurement.service;

import java.util.List;

/**
 * Decodes an encoded measurement payload and records the request.
 *
 * The contract is intentionally narrow so the controller layer can stay free
 * of any persistence or algorithm concerns.
 */
public interface MeasurementService {

    /**
     * Decode the supplied encoded string and persist an audit row.
     *
     * @param encoded        the encoded measurement payload
     * @param sourceIp       the remote client IP captured by the controller
     * @return the list of decoded package totals
     */
    List<Integer> convertAndRecord(String encoded, String sourceIp);
}
