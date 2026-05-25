package com.oraclequantapi.oraclequantapi.alharith_alsubhi.packageconversion.service;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies ConversionEngine against all PDF example inputs.
 * Hint: Run with mvn test — no Spring context needed (fast unit tests).
 */
public class ConversionEngineTest {

    // --- Official evaluation examples (must match auto-grader expected output) ---

    @Test
    public void testExample1_aa() {
        List<Integer> result = ConversionEngine.convert("aa");
        assertEquals(List.of(1), result);
    }

    @Test
    public void testExample2_abbcc() {
        List<Integer> result = ConversionEngine.convert("abbcc");
        assertEquals(List.of(2, 6), result);
    }

    @Test
    public void testExample3_dz_a_aazzaaa() {
        List<Integer> result = ConversionEngine.convert("dz_a_aazzaaa");
        assertEquals(List.of(28, 53, 1), result);
    }

    @Test
    public void testExample4_a_() {
        List<Integer> result = ConversionEngine.convert("a_");
        assertEquals(List.of(0), result);
    }

    @Test
    public void testExample5_abcdabcdab() {
        List<Integer> result = ConversionEngine.convert("abcdabcdab");
        assertEquals(List.of(2, 7, 7), result);
    }

    @Test
    public void testExample6_abcdabcdab_() {
        List<Integer> result = ConversionEngine.convert("abcdabcdab_");
        assertEquals(List.of(2, 7, 7, 0), result);
    }

    @Test
    public void testExample7_zdaaaaaaaabaaaaaaaabaaaaaaaabbaa() {
        List<Integer> result = ConversionEngine.convert("zdaaaaaaaabaaaaaaaabaaaaaaaabbaa");
        assertEquals(List.of(34), result);
    }

    @Test
    public void testExample8_za_a_a_a_a_a_a_a_a_a_a_a_a_azaaa() {
        List<Integer> result = ConversionEngine.convert("za_a_a_a_a_a_a_a_a_a_a_a_a_azaaa");
        assertEquals(List.of(40, 1), result);
    }

    // --- Extra edge cases (robustness, not all in PDF) ---

    @Test
    public void testEdgeCases() {
        // Null and empty strings
        assertTrue(ConversionEngine.convert(null).isEmpty());
        assertTrue(ConversionEngine.convert("").isEmpty());
        assertTrue(ConversionEngine.convert("   ").isEmpty());

        // Case insensitivity
        assertEquals(List.of(1), ConversionEngine.convert("AA"));
        assertEquals(List.of(2, 6), ConversionEngine.convert("aBBcc"));

        // Truncated package counts/values gracefully handled
             // Count is 2, but no values follow (sums available = 0)
        assertEquals(List.of(0), ConversionEngine.convert("b"));

             // Count is 2, but only 1 value follows (sums available = 1)
        assertEquals(List.of(1), ConversionEngine.convert("ba"));
    }
}
