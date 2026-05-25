package com.oraclequantapi.oraclequantapi;

import com.oraclequantapi.oraclequantapi.services.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ConverterTest {

    private Converter converter;

    @BeforeEach
    void setUp() {
        converter = new Converter();
    }

    @Test
    void testAa() {
        assertEquals(List.of(1), converter.Convert("aa"));
    }

    @Test
    void testAbbcc() {
        assertEquals(List.of(2, 6), converter.Convert("abbcc"));
    }

    @Test
    void testDz_a_aazzaaa() {
        assertEquals(List.of(28, 53, 1), converter.Convert("dz_a_aazzaaa"));
    }

    @Test
    void testA_() {
        assertEquals(List.of(0), converter.Convert("a_"));
    }

    @Test
    void testAbcdabcdab() {
        assertEquals(List.of(2, 7, 7), converter.Convert("abcdabcdab"));
    }

    @Test
    void testAbcdabcdab_() {
        assertEquals(List.of(2, 7, 7, 0), converter.Convert("abcdabcdab_"));
    }

    @Test
    void testZdaaaaaaaabaaaaaaaabaaaaaaaabbaa() {
        assertEquals(List.of(34), converter.Convert("zdaaaaaaaabaaaaaaaabaaaaaaaabbaa"));
    }

    @Test
    void testZa_a_a_a_a_a_a_a_a_a_a_a_a_azaaa() {
        assertEquals(List.of(40, 1), converter.Convert("za_a_a_a_a_a_a_a_a_a_a_a_a_azaaa"));
    }

    @Test
    void testInvalidInputReturnsNull() {
        assertNull(converter.Convert("123"));
        assertNull(converter.Convert("a b"));
        assertNull(converter.Convert(""));
    }
}
