package io.github.hylexus.jt.utils;

import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DefaultBitStreamReaderTest {

    @Test
    void testReadBit() {
        try (BitStreamReader reader = BitStreamReader.fromByteBuf(ByteBufAllocator.DEFAULT.buffer().writeShort(0b0110101011100010))) {
            Assertions.assertEquals(0, reader.readBit());
            Assertions.assertEquals(1, reader.readBit());
            Assertions.assertEquals(1, reader.readBit());

            Assertions.assertEquals(0, reader.readBit());
            Assertions.assertEquals(1, reader.readBit());
            Assertions.assertEquals(0, reader.readBit());

            Assertions.assertEquals(1, reader.readBit());
            Assertions.assertEquals(0, reader.readBit());
            Assertions.assertEquals(1, reader.readBit());

            Assertions.assertEquals(1, reader.readBit());
            Assertions.assertEquals(1, reader.readBit());
            Assertions.assertEquals(0, reader.readBit());

            Assertions.assertEquals(0, reader.readBit());
            Assertions.assertEquals(0, reader.readBit());
            Assertions.assertEquals(1, reader.readBit());

            Assertions.assertEquals(0, reader.readBit());
        }
    }

    @Test
    void testReadBitWithLength() {
        try (BitStreamReader reader = BitStreamReader.fromByteBuf(ByteBufAllocator.DEFAULT.buffer().writeShort(0b0110101011100010))) {
            Assertions.assertEquals("011", FormatUtils.toBinaryString(reader.readBit(3), 3));
            Assertions.assertEquals("010", FormatUtils.toBinaryString(reader.readBit(3), 3));
            Assertions.assertEquals("101", FormatUtils.toBinaryString(reader.readBit(3), 3));
            Assertions.assertEquals("110", FormatUtils.toBinaryString(reader.readBit(3), 3));
            Assertions.assertEquals("0", FormatUtils.toBinaryString(reader.readBit(1)));
        }
    }

    @Test
    void testUe() {
        try (BitStreamReader reader = BitStreamReader.fromByteBuf(ByteBufAllocator.DEFAULT.buffer().writeByte(0b00101000))) {
            Assertions.assertEquals(4, reader.readUe());
        }
    }

    @Test
    void testSe() {
        try (BitStreamReader reader = BitStreamReader.fromByteBuf(ByteBufAllocator.DEFAULT.buffer().writeByte(0b000_1011_0))) {
            Assertions.assertEquals(-5, reader.readSe());
        }
        try (BitStreamReader reader = BitStreamReader.fromByteBuf(ByteBufAllocator.DEFAULT.buffer().writeByte(0b000_1010_0))) {
            Assertions.assertEquals(5, reader.readSe());
        }
        try (BitStreamReader reader = BitStreamReader.fromByteBuf(ByteBufAllocator.DEFAULT.buffer().writeByte(0b000_1111_0))) {
            Assertions.assertEquals(-7, reader.readSe());
        }
        try (BitStreamReader reader = BitStreamReader.fromByteBuf(ByteBufAllocator.DEFAULT.buffer().writeByte(0b000_1110_0))) {
            Assertions.assertEquals(7, reader.readSe());
        }
    }

}