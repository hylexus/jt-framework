package io.github.hylexus.jt.jt1078.support.extension.h264;

import io.github.hylexus.jt.jt1078.support.extension.h264.impl.DefaultH264NaluHeader;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * NALU(Network Abstraction Layer Unit)
 */
public interface H264NaluHeader {

    static H264NaluHeader of(int value) {
        return new DefaultH264NaluHeader(value);
    }

    /**
     * bit[7] F 禁止位
     * <p>
     * 正常情况下为 0,
     * 某些情况下，如果 NALU 发生了丢失数据的情况可以将该字段置为 1，以便接收方纠错或直接丢掉该 NALU
     */
    default byte forbiddenBit() {
        return 0;
    }

    /**
     * bit[1,2] NRI 表示该 NALU 的重要性(可以作为该 NALU 是否可以被丢弃的标识)
     * <p>
     * 取值范围: [1,3]
     * <p>
     * 0: DISPOSABLE;
     * 1: LOW;
     * 2: HIGH;
     * 3: HIGHEST
     */
    byte nalRefIdc();

    default byte nir() {
        return this.nalRefIdc();
    }

    /**
     * bit[3,8) TYPE
     */
    byte typeValue();

    default Optional<NaluType> type() {
        return NaluType.findByCode(typeValue());
    }

    @Getter
    enum NaluType {
        /**
         * 未指定
         */
        UNKNOWN((byte) 0),
        /**
         * slice_layer_without_partitioning_rbsp()
         * <p>
         * 一般都是除了 {@link #IDR} 之外的其他视频数据, 有可能是一个 I 帧
         */
        SLICE((byte) 1),
        /**
         * slice_data_partition_a_layer_rbsp()
         */
        SDP_A((byte) 2),
        /**
         * slice_data_partition_b_layer_rbsp()
         */
        SDP_B((byte) 3),
        /**
         * slice_data_partition_c_layer_rbsp()
         */
        SDP_C((byte) 4),
        /**
         * Instantaneous decoding refresh
         * <p>
         * IDR 一定是 I 帧，反过来不成立
         */
        IDR((byte) 5),
        /**
         * Supplemental enhancement information
         * <p>
         * 自定义信息(对应的 NRI 一般是 0)
         */
        SEI((byte) 6),
        /**
         * Sequence Parameter Set
         */
        SPS((byte) 7),
        /**
         * Picture Parameter Set
         */
        PPS((byte) 8),
        /**
         * Access unit delimiter
         */
        AUD((byte) 9),
        /**
         * End of sequence RBSP
         */
        END_OF_SEQ((byte) 10),
        /**
         * End of stream RBSP
         */
        END_OF_STREAM((byte) 11),
        /**
         * Filler data RBSP
         */
        FILTER_DATA((byte) 12),
        /**
         * seq_parameter_set_extension_rbsp()
         */
        SPS_EXT((byte) 13),
        ;
        private final byte value;

        private static final Map<Byte, NaluType> mapping = new HashMap<>(values().length);

        static {
            for (final NaluType value : values()) {
                mapping.put(value.value, value);
            }
        }

        NaluType(byte value) {
            this.value = value;
        }

        public static Optional<NaluType> findByCode(byte code) {
            return Optional.ofNullable(mapping.get(code));
        }
    }
}
