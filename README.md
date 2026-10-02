# Java Buffers Library

A lightweight, high-performance Java library providing various buffer implementations designed for optimized memory and data management. Built with a focus on flexibility, clean design, and efficient off-heap resource handling.

## Core Architecture

The library revolves around clean interfaces located in the `com.buffers` package:
* **`Buffer`**: The foundational interface defining core read operations (`getByte`, `getCapacity`, `getLength`), along with built-in support for byte iteration and Java Streams.
* **`MutableBuffer`**: An extension of `Buffer` that adds mutation capabilities, allowing safe byte writes at specific offsets via `putByte`.

---

## AlignedBuffer

The `com.buffers.aligned` package introduces **`AlignedBuffer`**, which leverages modern Java Foreign Function & Memory API (Project Panama) features to work with strictly aligned off-heap memory segments.

### Key Features
* **Strict Memory Alignment:** Supports hardware-friendly memory boundaries ranging from 4 bytes up to 64 KB (`Alignment` enum), critical for performance-sensitive operations and low-level I/O.
* **Automatic Capacity Rounding:** The `Alignment` utility seamlessly handles bitwise masks and boundary rounding (`roundUp` / `roundDown`) to ensure compliance with memory architecture requirements.
* **Arena Integration:** Backed by `MemorySegment` and managed through standard Java `Arena` scopes, ensuring deterministic lifecycle management and safety.
* **Full Buffer Compliance:** Implements `MutableBuffer`, enabling standard iteration and stream processing over populated regions.

### Usage Example

```java
import java.lang.foreign.Arena;

import com.buffers.aligned.AlignedBuffer;
import com.buffers.aligned.Alignment;

try (final Arena arena = Arena.ofConfined()) {
    // Allocate a buffer with 1024 bytes capacity, aligned to 64-byte boundaries
    final AlignedBuffer buffer = AlignedBuffer.allocate(1024, Alignment.BYTE_64, arena);

    // Write bytes
    buffer.putByte(0, (byte) 42);
    buffer.putByte(1, (byte) 100);

    // Read bytes and inspect metadata
    System.out.println("Capacity: " + buffer.getCapacity());
    System.out.println("Length: " + buffer.getLength());
    System.out.println("Byte at 0: " + buffer.getByte(0));
}