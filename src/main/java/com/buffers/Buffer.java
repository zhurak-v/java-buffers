package com.buffers;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Represents a read-only view of a fixed-capacity byte buffer.
 * Provides basic access operations, length tracking, and stream/iterable support.
 */
public interface Buffer extends Iterable<Byte> {

    /**
     * Returns the total allocated capacity of this buffer in bytes[cite: 3].
     *
     * @return the buffer capacity
     */
    long getCapacity();

    /**
     * Returns the current logical length (number of active/written bytes) of this buffer[cite: 3].
     *
     * @return the buffer length
     */
    long getLength();

    /**
     * Retrieves the byte at the specified offset[cite: 3].
     *
     * @param offset the index of the byte to read
     * @return the byte value at the given offset
     */
    byte getByte(long offset);

    /**
     * Returns a sequential {@code Stream} over the bytes contained within this buffer[cite: 3].
     *
     * @return a stream of bytes
     */
    default Stream<Byte> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}