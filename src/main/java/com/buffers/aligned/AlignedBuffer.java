package com.buffers.aligned;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.buffers.MutableBuffer;

/**
 * An aligned off-heap memory buffer implementation backed by Java's Foreign Function & Memory API (Project Panama).
 * Provides hardware-aligned memory allocations for high-performance and low-level system operations.
 */
public final class AlignedBuffer implements MutableBuffer {

    private final Alignment memoryAlignment;
    private final long capacity;
    private long length;

    private final MemorySegment memorySegment;

    /**
     * Constructs a new {@code AlignedBuffer} with the specified capacity, memory alignment, and memory arena.
     * The allocated capacity is automatically rounded up to match the alignment requirements.
     *
     * @param capacity        the requested capacity of the buffer in bytes
     * @param memoryAlignment the desired memory alignment constraint
     * @param arena           the arena managing the lifecycle of the allocated memory segment
     */
    private AlignedBuffer(
            final long capacity,
            final Alignment memoryAlignment,
            final Arena arena
    ) {
        this.memoryAlignment = memoryAlignment;
        this.capacity = this.memoryAlignment.roundUp(capacity);
        this.length = 0;
        this.memorySegment = arena.allocate(this.capacity, this.memoryAlignment.getValue());
    }

    /**
     * Allocates a new aligned memory buffer using the provided arena and alignment settings.
     *
     * @param capacity        the requested capacity of the buffer in bytes
     * @param memoryAlignment the memory alignment constraint
     * @param arena           the arena that controls the memory segment's allocation scope
     * @return a newly allocated {@code AlignedBuffer} instance
     */
    public static AlignedBuffer allocate(
            final long capacity,
            final Alignment memoryAlignment,
            final Arena arena
    ) {
        return new AlignedBuffer(capacity, memoryAlignment, arena);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getByte(final long offset) {
        return this.memorySegment.get(ValueLayout.JAVA_BYTE, offset);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Automatically updates the buffer's logical length if the write operation extends beyond the current length.
     */
    @Override
    public void putByte(
            final long offset,
            final byte value
    ) {
        this.memorySegment.set(ValueLayout.JAVA_BYTE, offset, value);
        this.length = Math.max(this.length, offset + Byte.BYTES);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getCapacity() {
        return this.capacity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLength() {
        return this.length;
    }

    /**
     * Returns the memory alignment configuration associated with this buffer.
     *
     * @return the {@link Alignment} instance used during allocation
     */
    public Alignment getMemoryAlignment() {
        return this.memoryAlignment;
    }

    /**
     * Returns the underlying native memory segment managed by this buffer.
     *
     * @return the raw {@link MemorySegment}
     */
    public MemorySegment getMemorySegment() {
        return this.memorySegment;
    }

    /**
     * Returns an iterator over the written bytes in the buffer up to its current length.
     *
     * @return an {@link Iterator} of {@link Byte} objects
     */
    @Override
    public Iterator<Byte> iterator() {
        return new Iterator<>() {

            private long offset;

            @Override
            public boolean hasNext() {
                return this.offset < AlignedBuffer.this.length;
            }

            @Override
            public Byte next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }

                return AlignedBuffer.this.getByte(this.offset++);
            }
        };
    }
}