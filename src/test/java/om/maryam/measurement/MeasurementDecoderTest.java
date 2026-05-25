package om.maryam.measurement;

import om.maryam.measurement.algorithm.MeasurementDecoder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Validates the decoder against every example from the evaluation document.
 */
class MeasurementDecoderTest {

    private final MeasurementDecoder decoder = new MeasurementDecoder();

    @Test
    void decode_singleLetter() {
        assertThat(decoder.decode("aa")).isEqualTo(List.of(1));
    }

    @Test
    void decode_twoPackets() {
        assertThat(decoder.decode("abbcc")).isEqualTo(List.of(2, 6));
    }

    @Test
    void decode_continuationAndTerminator() {
        assertThat(decoder.decode("dz_a_aazzaaa")).isEqualTo(List.of(28, 53, 1));
    }

    @Test
    void decode_lonelyTerminator() {
        assertThat(decoder.decode("a_")).isEqualTo(List.of(0));
    }

    @Test
    void decode_threeIdenticalPackets() {
        assertThat(decoder.decode("abcdabcdab")).isEqualTo(List.of(2, 7, 7));
    }

    @Test
    void decode_trailingUnderscoreCreatesEmptyPacket() {
        assertThat(decoder.decode("abcdabcdab_")).isEqualTo(List.of(2, 7, 7, 0));
    }

    @Test
    void decode_largeCountViaZChain() {
        assertThat(decoder.decode("zdaaaaaaaabaaaaaaaabaaaaaaaabbaa"))
                .isEqualTo(List.of(34));
    }

    @Test
    void decode_zMixedWithUnderscores() {
        assertThat(decoder.decode("za_a_a_a_a_a_a_a_a_a_a_a_a_azaaa"))
                .isEqualTo(List.of(40, 1));
    }

    @Test
    void decode_emptyInput() {
        assertThat(decoder.decode("")).isEmpty();
        assertThat(decoder.decode(null)).isEmpty();
    }
}
