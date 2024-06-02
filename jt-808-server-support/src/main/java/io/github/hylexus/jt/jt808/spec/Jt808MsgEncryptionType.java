package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.utils.FormatUtils;

public interface Jt808MsgEncryptionType {

    int intValue();

    default int bit10() {
        return this.intValue() & 0b001;
    }

    default int bit11() {
        return this.intValue() & 0b010;
    }

    default int bit12() {
        return this.intValue() & 0b100;
    }

    default boolean isEncrypted() {
        return this.intValue() != 0;
    }

    static Jt808MsgEncryptionType fromMsgBodyProps(int bodyPros) {
        // bit[10-12] 0001,1100,0000,0000(1C00)(加密类型)
        return new Default((bodyPros & 0x1c00) >> 10);
    }

    static Jt808MsgEncryptionType fromIntValue(int value) {
        return new Default(value & 0b111);
    }

    static Jt808MsgEncryptionType fromBits(int bit10, int bit11, int bit12) {
        return new Default(
                (bit10 & 0b1)
                        | ((bit11 << 1) & 0b10)
                        | ((bit12 << 2) & 0b100)
        );
    }


    class Default implements Jt808MsgEncryptionType {
        private final int value;

        public Default(int value) {
            this.value = value;
        }

        @Override
        public int intValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return "Default{"
                    + "value=" + value
                    + "(" + FormatUtils.toBinaryString(value, 3) + ")"
                    + '}';
        }
    }
}
