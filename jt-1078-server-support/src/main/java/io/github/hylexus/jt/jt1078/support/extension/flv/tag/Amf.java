package io.github.hylexus.jt.jt1078.support.extension.flv.tag;

import io.github.hylexus.oaks.utils.IntBitOps;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.List;

public interface Amf {

    int writeTo(ByteBuf buffer);

    static Amf ofTerminator() {
        return AfmTerminator.INSTANCE;
    }

    static Amf ofString(String value) {
        return new AmfString(value);
    }

    static Amf ofNumber(Double number) {
        return new AmfNumber(number);
    }

    static Amf ofPair(Pair pair) {
        return new AmfPair(pair);
    }

    static Amf ofPair(String key, Amf value) {
        return new AmfPair(new Pair(key, value));
    }

    static Amf ofEcmaArray(List<Amf> list) {
        return new AmfEcmaArray(list);
    }

    abstract class AbstractAmf<T> implements Amf {
        protected final T value;

        public AbstractAmf(T value) {
            this.value = value;
        }

        @Override
        public int writeTo(ByteBuf buffer) {
            int writerIndex = buffer.writerIndex();
            this.doWrite(buffer);
            return buffer.writerIndex() - writerIndex;
        }

        abstract void doWrite(ByteBuf buffer);
    }

    class AmfString extends AbstractAmf<String> {
        public AmfString(String value) {
            super(value);
        }

        @Override
        void doWrite(ByteBuf buffer) {
            buffer.writeByte(DataType.STRING.getValue());
            final byte[] bytes = this.value.getBytes(StandardCharsets.UTF_8);
            buffer.writeShort(bytes.length);
            buffer.writeBytes(bytes);
        }
    }

    class AmfNumber extends AbstractAmf<Double> {

        public AmfNumber(Double value) {
            super(value);
        }

        @Override
        void doWrite(ByteBuf buffer) {
            buffer.writeByte(DataType.NUMBER.getValue());
            buffer.writeDouble(this.value);
        }

    }

    class AmfBoolean extends AbstractAmf<Boolean> {
        public AmfBoolean(Boolean value) {
            super(value);
        }

        @Override
        void doWrite(ByteBuf buffer) {
            buffer.writeByte(DataType.BOOLEAN.getValue());
            buffer.writeByte(this.value ? 1 : 0);
        }
    }

    class Pair {
        private final String key;
        private final Amf value;

        public Pair(String key, Amf value) {
            this.key = key;
            this.value = value;
        }
    }

    class AmfPair extends AbstractAmf<Pair> {
        public AmfPair(Pair value) {
            super(value);
        }

        @Override
        void doWrite(ByteBuf buffer) {
            // buffer.writeByte(DataType.OBJECT.getValue());

            final byte[] keyBytes = value.key.getBytes(StandardCharsets.UTF_8);
            buffer.writeShort(keyBytes.length);
            buffer.writeBytes(keyBytes);
            value.value.writeTo(buffer);

            // Amf.ofTerminator().writeTo(buffer);
        }
    }

    class AmfEcmaArray extends AbstractAmf<List<Amf>> {
        public AmfEcmaArray(List<Amf> value) {
            super(value);
        }

        @Override
        void doWrite(ByteBuf buffer) {
            // 1 + 4 + n
            buffer.writeByte(DataType.ECMA_ARRAY.getValue());
            buffer.writeInt(this.value.size());
            for (Amf value : this.value) {
                value.writeTo(buffer);
            }
        }
    }

    enum AfmTerminator implements Amf {
        INSTANCE;

        @Override
        public int writeTo(ByteBuf buffer) {
            buffer.writeBytes(IntBitOps.intTo3Bytes(DataType.OBJECT_END_MARKER.getValue()));
            return 3;
        }
    }

    @Getter
    enum DataType {
        /**
         * 类型: Double
         * <p>
         * 格式: type[UI8](0) + value[8bytes]
         */
        NUMBER((byte) 0),
        /**
         * 格式: type[UI8](1) + value[1byte]
         */
        BOOLEAN((byte) 1),
        /**
         * 长度不超过 65535
         * <p>
         * 格式: type[UI8](2) + length[UI16] + data[String]
         *
         * @see #LONG_STRING
         */
        STRING((byte) 2),
        /**
         * 格式: type[UI8] + name[String] + k1Type[UI8] + k1Value[] +k2Type[UI8] + k2Value[] + ... + terminator[OBJECT_END_MARKER]
         *
         * @see #OBJECT_END_MARKER
         */
        OBJECT((byte) 3),
        MOVIE_CLIP((byte) 4),
        NULL((byte) 5),
        UNDEFINED((byte) 6),
        REFERENCE((byte) 7),
        /**
         * 格式: type[UI8] + length[UI32] + value + terminator[VARIABLE_END]
         */
        ECMA_ARRAY((byte) 8),
        /**
         * 类型: UI24, 始终为 9
         *
         * @see #OBJECT
         */
        OBJECT_END_MARKER((byte) 9),
        /**
         * 格式: type[UI8] + length[UI32] + value + terminator[]
         */
        STRICT_ARRAY((byte) 10),
        DATE((byte) 11),

        /**
         * 长度超过 65535
         * <p>
         * 格式: length[UI32] + data[String]
         *
         * @see #STRING
         */
        LONG_STRING((byte) 12),
        ;
        private final byte value;

        DataType(byte value) {
            this.value = value;
        }
    }
}
