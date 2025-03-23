/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hylexus.jt.jt808.support.annotation.msg;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 从 xtream-codec 复制过来的
 *
 * @author hylexus
 * @see <a href="https://github.com/hylexus/xtream-codec/blob/372c9ab613738790631c14d546379fea36e3c0e7/xtream-codec-core/src/main/java/io/github/hylexus/xtream/codec/core/annotation/PrependLengthFieldType.java#L21">https://github.com/hylexus/xtream-codec/blob/372c9ab613738790631c14d546379fea36e3c0e7/xtream-codec-core/src/main/java/io/github/hylexus/xtream/codec/core/annotation/PrependLengthFieldType.java#L21</a>
 */
public enum PrependLengthFieldType {
    none(-1) {
        @Override
        public int readFrom(ByteBuf buffer) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeTo(ByteBuf buffer, int value) {
            throw new UnsupportedOperationException();
        }
    },
    u8(1) {
        @Override
        public int readFrom(ByteBuf buffer) {
            return buffer.readUnsignedByte();
        }

        @Override
        public void writeTo(ByteBuf buffer, int value) {
            buffer.writeByte(value);
        }
    },
    u16(2) {
        @Override
        public int readFrom(ByteBuf buffer) {
            return buffer.readUnsignedShort();
        }

        @Override
        public void writeTo(ByteBuf buffer, int value) {
            buffer.writeShort(value);
        }
    },
    u32(4) {
        @Override
        public int readFrom(ByteBuf buffer) {
            return (int) buffer.readUnsignedInt();
        }

        @Override
        public void writeTo(ByteBuf buffer, int value) {
            buffer.writeInt(value);
        }
    },
    ;
    final int byteCounts;

    PrependLengthFieldType(int byteCounts) {
        this.byteCounts = byteCounts;
    }

    public abstract int readFrom(ByteBuf buffer);

    public abstract void writeTo(ByteBuf buffer, int value);

    @SuppressWarnings("lombok")
    public int getByteCounts() {
        return byteCounts;
    }

    private static final Map<String, PrependLengthFieldType> MAPPING = Arrays.stream(values())
            .collect(Collectors.toMap(Enum::name, it -> it));

    public static PrependLengthFieldType of(String name) {
        return MAPPING.get(name);
    }

}
