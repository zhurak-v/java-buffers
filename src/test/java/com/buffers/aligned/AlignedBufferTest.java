package com.buffers.aligned;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

final class AlignedBufferTest {

    AlignedBufferTest() {}

    @Test
    @DisplayName("Should successfully allocate buffer with aligned capacity")
    void shouldAllocateBufferWithAlignedCapacity() {
        try (final Arena arena = Arena.ofConfined()) {
            final AlignedBuffer buffer = AlignedBuffer.allocate(10L, Alignment.BYTE_16, arena);

            assertAll(
                    () -> assertEquals(16L, buffer.getCapacity()),
                    () -> assertEquals(0L, buffer.getLength()),
                    () -> assertEquals(Alignment.BYTE_16, buffer.getMemoryAlignment()),
                    () -> assertNotNull(buffer.getMemorySegment())
            );
        }
    }

    @Test
    @DisplayName("Should correctly put and get bytes while updating length")
    void shouldPutAndGetByteCorrectly() {
        try (final Arena arena = Arena.ofConfined()) {
            final AlignedBuffer buffer = AlignedBuffer.allocate(32L, Alignment.BYTE_8, arena);

            buffer.putByte(0L, (byte) 10);
            buffer.putByte(5L, (byte) 42);

            assertAll(
                    () -> assertEquals((byte) 10, buffer.getByte(0L)),
                    () -> assertEquals((byte) 42, buffer.getByte(5L)),
                    () -> assertEquals(6L, buffer.getLength())
            );
        }
    }

    @Test
    @DisplayName("Should provide a valid iterator over written data")
    void shouldProvideValidIterator() {
        try (final Arena arena = Arena.ofConfined()) {
            final AlignedBuffer buffer = AlignedBuffer.allocate(16L, Alignment.BYTE_4, arena);

            buffer.putByte(0L, (byte) 1);
            buffer.putByte(1L, (byte) 2);
            buffer.putByte(2L, (byte) 3);

            final Iterator<Byte> iterator = buffer.iterator();

            assertAll(
                    () -> assertTrue(iterator.hasNext()),
                    () -> assertEquals((byte) 1, iterator.next()),
                    () -> assertTrue(iterator.hasNext()),
                    () -> assertEquals((byte) 2, iterator.next()),
                    () -> assertTrue(iterator.hasNext()),
                    () -> assertEquals((byte) 3, iterator.next()),
                    () -> assertFalse(iterator.hasNext())
            );

            assertThrows(NoSuchElementException.class, iterator::next);
        }
    }

    @Test
    @DisplayName("Should support Stream API operations")
    void shouldSupportStreamApi() {
        try (final Arena arena = Arena.ofConfined()) {
            final AlignedBuffer buffer = AlignedBuffer.allocate(16L, Alignment.BYTE_4, arena);
            buffer.putByte(0L, (byte) 10);
            buffer.putByte(1L, (byte) 20);

            final long count = buffer.stream().count();
            assertEquals(2L, count);
            assertEquals(30, buffer.stream().mapToInt(Byte::intValue).sum());
        }
    }
}