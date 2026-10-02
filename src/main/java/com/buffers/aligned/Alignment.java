package com.buffers.aligned;

/**
 * Represents standard memory alignment constraints (in bytes) used for hardware-friendly
 * and off-heap memory allocations. Supported alignments range from 4 bytes up to 64 KB.
 */
public enum Alignment {

    BYTE_4(1L << 2),
    BYTE_8(1L << 3),
    BYTE_16(1L << 4),
    BYTE_32(1L << 5),
    BYTE_64(1L << 6),
    BYTE_128(1L << 7),
    BYTE_256(1L << 8),
    BYTE_512(1L << 9),
    BYTE_1024(1L << 10),
    BYTE_2048(1L << 11),
    BYTE_4096(1L << 12),
    BYTE_8192(1L << 13),
    BYTE_16384(1L << 14),
    BYTE_32768(1L << 15),
    BYTE_65536(1L << 16);

    private final long value;
    private final long mask;

    /**
     * Constructs an alignment constant with the specified byte value and pre-calculates its bitmask.
     *
     * @alignmentValue the alignment boundary in bytes (must be a power of two)
     */
    Alignment(final long alignmentValue) {
        this.value = alignmentValue;
        this.mask = alignmentValue - 1L;
    }

    /**
     * Returns the numeric byte value of this alignment.
     *
     * @return the alignment value in bytes
     */
    public long getValue() {
        return this.value;
    }

    /**
     * Rounds up the target length to the nearest multiple of this alignment boundary.
     *
     * @param targetLength the length to round up
     * @return the aligned length (greater than or equal to targetLength)
     * @throws IllegalArgumentException if {@code targetLength} is negative
     */
    public long roundUp(final long targetLength) {
        if (targetLength < 0) {
            throw new IllegalArgumentException("Target length cannot be negative");
        }

        return (targetLength + this.mask) & ~this.mask;
    }

    /**
     * Rounds down the target offset to the nearest multiple of this alignment boundary.
     *
     * @param targetOffset the offset to round down
     * @return the aligned offset (less than or equal to targetOffset)
     * @throws IllegalArgumentException if {@code targetOffset} is negative
     */
    public long roundDown(final long targetOffset) {
        if (targetOffset < 0) {
            throw new IllegalArgumentException("Target offset cannot be negative");
        }

        return targetOffset & ~this.mask;
    }

    /**
     * Rounds up the target length using the specified alignment.
     *
     * @param targetLength the length to round up
     * @param alignment    the alignment boundary to apply
     * @return the aligned length
     */
    public static long roundUp(final long targetLength, final Alignment alignment) {
        return alignment.roundUp(targetLength);
    }

    /**
     * Rounds down the target offset using the specified alignment.
     *
     * @param targetOffset the offset to round down
     * @param alignment    the alignment boundary to apply
     * @return the aligned offset
     */
    public static long roundDown(final long targetOffset, final Alignment alignment) {
        return alignment.roundDown(targetOffset);
    }

    /**
     * Finds and returns the {@code Alignment} constant corresponding to the given byte value.
     *
     * @alignmentValue the numeric byte value to search for
     * @return the matching {@code Alignment} enum constant
     * @throws IllegalArgumentException if no matching alignment is found
     */
    public static Alignment fromValue(final long alignmentValue) {
        for (final Alignment alignment : Alignment.values()) {
            if (alignment.getValue() == alignmentValue) {
                return alignment;
            }
        }
        throw new IllegalArgumentException("Value " + alignmentValue + " is not a valid alignment value");
    }
}