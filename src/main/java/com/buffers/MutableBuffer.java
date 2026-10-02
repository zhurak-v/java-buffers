package com.buffers;

/**
 * Represents a mutable byte buffer that supports both reading and writing operations.
 */
public interface MutableBuffer extends Buffer {

    /**
     * Stores the specified byte at the given offset within the buffer.
     *
     * @param offset the index at which to write the byte
     * @param value  the byte value to store
     */
    void putByte(final long offset, final byte value);

}