package com.buffers.aligned;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

final class AlignmentTest {

    AlignmentTest() {}

    @Test
    @DisplayName("Should return the correct enum value")
    void shouldReturnCorrectValue() {
        assertEquals(4L, Alignment.BYTE_4.getValue());
        assertEquals(64L, Alignment.BYTE_64.getValue());
        assertEquals(65536L, Alignment.BYTE_65536.getValue());
    }

    @ParameterizedTest
    @CsvSource({
            "BYTE_4, 0, 0",
            "BYTE_4, 1, 4",
            "BYTE_4, 4, 4",
            "BYTE_4, 5, 8",
            "BYTE_64, 63, 64",
            "BYTE_64, 64, 64",
            "BYTE_64, 65, 128"
    })
    @DisplayName("Should round up target length correctly")
    void shouldRoundUpCorrectly(final String alignmentName, final long targetLength, final long expected) {
        final Alignment alignment = Alignment.valueOf(alignmentName);
        assertEquals(expected, alignment.roundUp(targetLength));
        assertEquals(expected, Alignment.roundUp(targetLength, alignment));
    }

    @Test
    @DisplayName("Should throw exception when rounding up a negative length")
    void shouldThrowExceptionOnNegativeRoundUp() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Alignment.BYTE_16.roundUp(-1L)
        );
        assertEquals("Target length cannot be negative", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "BYTE_4, 0, 0",
            "BYTE_4, 3, 0",
            "BYTE_4, 4, 4",
            "BYTE_4, 7, 4",
            "BYTE_64, 63, 0",
            "BYTE_64, 64, 64",
            "BYTE_64, 127, 64"
    })
    @DisplayName("Should round down target offset correctly")
    void shouldRoundDownCorrectly(String alignmentName, long targetOffset, long expected) {
        final Alignment alignment = Alignment.valueOf(alignmentName);
        assertEquals(expected, alignment.roundDown(targetOffset));
        assertEquals(expected, Alignment.roundDown(targetOffset, alignment));
    }

    @Test
    @DisplayName("Should throw exception when rounding down a negative offset")
    void shouldThrowExceptionOnNegativeRoundDown() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Alignment.BYTE_16.roundDown(-5L)
        );
        assertEquals("Target offset cannot be negative", exception.getMessage());
    }

    @Test
    @DisplayName("Should find alignment from its numeric value")
    void shouldReturnAlignmentFromValue() {
        assertEquals(Alignment.BYTE_8, Alignment.fromValue(8L));
        assertEquals(Alignment.BYTE_1024, Alignment.fromValue(1024L));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L, 3L, 5L, 10L, 1000L})
    @DisplayName("Should throw exception for invalid alignment values")
    void shouldThrowExceptionForInvalidValue(long invalidValue) {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Alignment.fromValue(invalidValue)
        );
        assertTrue(exception.getMessage().contains("is not a valid alignment value"));
    }
}
