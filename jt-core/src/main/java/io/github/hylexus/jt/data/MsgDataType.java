package io.github.hylexus.jt.data;

import io.github.hylexus.jt.annotation.Jt808Field;
import lombok.Getter;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static io.github.hylexus.jt.config.JtProtocolConstant.FIELD_LENGTH_UNKNOWN;

/**
 * @author hylexus
 * Created At 2019-09-18 8:50 下午
 */
@Getter
public enum MsgDataType {
    BYTE(1, "无符号单字节整型(字节，8 位)", newHashSet(int.class, Integer.class, byte.class, Byte.class)),
    BYTES(0, "", newHashSet(byte[].class, Byte[].class)),
    WORD(2, "无符号双字节整型(字，16 位)", newHashSet(short.class, Short.class, int.class, Integer.class, long.class, Long.class)),
    DWORD(4, "无符号四字节整型(双字，32 位)", newHashSet(int.class, Integer.class, long.class, Long.class)),
    BCD(0, "8421 码，n 字节", newHashSet(String.class)),
    STRING(0, "GBK 编码，若无数据，置空", newHashSet(String.class)),
    COLLECTION(FIELD_LENGTH_UNKNOWN, "集合", newHashSet(List.class, Set.class));

    /**
     * 字节数
     * 为零表示使用外部指定的长度
     *
     * @see Jt808Field#length()
     */
    private int byteCount;

    private String desc;

    private Set<Class<?>> expectedTargetClassType;

    MsgDataType(int byteCount, String desc, Set<Class<?>> expectedTargetClassType) {
        this.byteCount = byteCount;
        this.desc = desc;
        this.expectedTargetClassType = expectedTargetClassType;
    }

}
