package om.maryam.measurement.algorithm;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Pure, side-effect free decoder for the OracleQuant measurement encoding.
 *
 * Encoding rules (derived from the evaluation specification):
 *
 *   - Letters carry a value : a = 1, b = 2, ... z = 26.
 *   - '_' (underscore)      : terminates the current "varint" with value 0.
 *   - 'z' is a continuation : when 'z' is encountered while reading a value
 *                             it contributes 26 to the running value AND
 *                             instructs the reader to continue with the next
 *                             character.  Any other character terminates the
 *                             value.
 *
 * The stream is a sequence of packets.  Each packet starts with a varint that
 * represents how many additional varints (call them "items") belong to the
 * packet.  The packet's reported total is the sum of those items.  The
 * algorithm then continues with the next packet until the input is exhausted.
 *
 * Worked example for "dz_a_aazzaaa" (expected [28, 53, 1]):
 *
 *   Packet 1 :
 *     header 'd'                  -> count = 4
 *     item 1: 'z' '_'             -> 26 + 0   = 26
 *     item 2: 'a'                 ->                1
 *     item 3: '_'                 ->                0
 *     item 4: 'a'                 ->                1
 *     packet total                                = 28
 *   Packet 2 :
 *     header 'a'                  -> count = 1
 *     item 1: 'z' 'z' 'a'         -> 26+26+1     = 53
 *   Packet 3 :
 *     header 'a'                  -> count = 1
 *     item 1: 'a'                 ->                1
 *
 * This component is stateless and therefore thread-safe; a single shared
 * Spring bean is sufficient for all incoming requests.
 */
@Component
public class MeasurementDecoder {

    private static final char CONTINUATION = 'z';
    private static final char TERMINATOR   = '_';

    /**
     * Decode an encoded measurement string into the list of packet totals.
     *
     * @param input the encoded payload (may be {@code null} or empty).
     * @return an immutable list of packet totals.  An empty input yields an
     *         empty list (never {@code null}).
     */
    public List<Integer> decode(String input) {
        if (input == null || input.isEmpty()) {
            return Collections.emptyList();
        }

        final Cursor cursor = new Cursor(input);
        final List<Integer> packets = new ArrayList<>();

        while (cursor.hasMore()) {
            final int count = readVarint(cursor);
            int total = 0;
            for (int i = 0; i < count && cursor.hasMore(); i++) {
                total += readVarint(cursor);
            }
            packets.add(total);
        }
        return Collections.unmodifiableList(packets);
    }

    /**
     * Reads a single variable-length integer at the current cursor position.
     * The cursor is advanced past every consumed character.
     */
    private int readVarint(Cursor cursor) {
        int value = 0;
        while (cursor.hasMore()) {
            final char c = cursor.next();
            if (c == TERMINATOR) {
                return value;
            }
            value += charValue(c);
            if (c != CONTINUATION) {
                return value;
            }
        }
        return value;
    }

    /**
     * Maps a lowercase letter to its 1-based positional value.
     * Non-letters fall through to 0 which is the safe neutral value.
     */
    private int charValue(char c) {
        if (c >= 'a' && c <= 'z') {
            return (c - 'a') + 1;
        }
        return 0;
    }

    /** Tiny mutable cursor over a {@link String}. */
    private static final class Cursor {
        private final String data;
        private int index;

        Cursor(String data) {
            this.data = data;
            this.index = 0;
        }

        boolean hasMore() {
            return index < data.length();
        }

        char next() {
            return data.charAt(index++);
        }
    }
}
