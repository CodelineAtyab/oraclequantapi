package om.maryam.measurement.exception;

/**
 * Thrown when a {@code ConversionHistory} row cannot be located by id.
 */
public class HistoryNotFoundException extends RuntimeException {

    public HistoryNotFoundException(Long id) {
        super("History record with id=" + id + " was not found");
    }
}
