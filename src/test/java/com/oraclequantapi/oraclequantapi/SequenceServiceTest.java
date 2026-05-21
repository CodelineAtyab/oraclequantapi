package com.oraclequantapi.oraclequantapi;

import com.oraclequantapi.oraclequantapi.service.SequenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SequenceServiceTest {

    private SequenceService service;

    @BeforeEach
    void setUp() {
        service = new SequenceService();
    }

    @Test
    void testAa() {
        assertEquals(List.of(1), service.processSequence(service.getSequence("aa")));
    }

    @Test
    void testAbbcc() {
        assertEquals(List.of(2, 6), service.processSequence(service.getSequence("abbcc")));
    }

    @Test
    void testDz_a_aazzaaa() {
        assertEquals(List.of(28, 53, 1), service.processSequence(service.getSequence("dz_a_aazzaaa")));
    }

    @Test
    void testA_() {
        assertEquals(List.of(0), service.processSequence(service.getSequence("a_")));
    }

    @Test
    void testAbcdabcdab() {
        assertEquals(List.of(2, 7, 7), service.processSequence(service.getSequence("abcdabcdab")));
    }

    @Test
    void testAbcdabcdab_() {
        assertEquals(List.of(2, 7, 7, 0), service.processSequence(service.getSequence("abcdabcdab_")));
    }

    @Test
    void testZdaaaaaaaabaaaaaaaabaaaaaaaabbaa() {
        assertEquals(List.of(34), service.processSequence(service.getSequence("zdaaaaaaaabaaaaaaaabaaaaaaaabbaa")));
    }

    @Test
    void testZa_a_a_a_a_a_a_a_a_a_a_a_a_azaaa() {
        assertEquals(List.of(40, 1), service.processSequence(service.getSequence("za_a_a_a_a_a_a_a_a_a_a_a_a_azaaa")));
    }
}
