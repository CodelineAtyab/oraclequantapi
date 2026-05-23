package com.oraclequantapi.oraclequantapi;

import com.oraclequantapi.oraclequantapi.service.SequenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the sequence conversion rules.
 *
 * Each test sends an encoded string into SequenceService and checks that the
 * returned package totals match the expected list.
 */
class SequenceServiceTest {

    private SequenceService service;

    // Create a fresh service before each test so tests do not share state.
    @BeforeEach
    void setUp() {
        service = new SequenceService();
    }

    // "a" reads one following value, and "a" as a value is 1.
    @Test
    void testAa() {
        assertEquals(List.of(1), service.processSequence(service.getSequence("aa")));
    }

    // First package: a -> one b value = 2. Second package: b -> two c values = 6.
    @Test
    void testAbbcc() {
        assertEquals(List.of(2, 6), service.processSequence(service.getSequence("abbcc")));
    }

    // Covers z and underscore handling in the encoded input.
    @Test
    void testDz_a_aazzaaa() {
        assertEquals(List.of(28, 1), service.processSequence(service.getSequence("dz_a_aazzaaa")));
    }

    // Underscore in a value position counts as 0.
    @Test
    void testA_() {
        assertEquals(List.of(0), service.processSequence(service.getSequence("a_")));
    }

    // Checks multiple packages in one continuous input string.
    @Test
    void testAbcdabcdab() {
        assertEquals(List.of(2, 7, 7), service.processSequence(service.getSequence("abcdabcdab")));
    }

    // A trailing underscore outside a complete package is returned as 0.
    @Test
    void testAbcdabcdab_() {
        assertEquals(List.of(2, 7, 7, 0), service.processSequence(service.getSequence("abcdabcdab_")));
    }

    // Covers a package count larger than 26 by using leading z in the count area.
    @Test
    void testZdaaaaaaaabaaaaaaaabaaaaaaaabbaa() {
        assertEquals(List.of(34), service.processSequence(service.getSequence("zdaaaaaaaabaaaaaaaabaaaaaaaabbaa")));
    }

    // Covers a long package where many underscores contribute zero to the total.
    @Test
    void testZa_a_a_a_a_a_a_a_a_a_a_a_a_azaaa() {
        assertEquals(List.of(40, 1), service.processSequence(service.getSequence("za_a_a_a_a_a_a_a_a_a_a_a_a_azaaa")));
    }
}
