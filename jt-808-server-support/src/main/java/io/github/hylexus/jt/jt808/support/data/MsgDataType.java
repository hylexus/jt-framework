package io.github.hylexus.jt.jt808.support.data;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

import java.util.List;
import java.util.Set;

import static java.util.Set.of;

/**
 * @author hylexus
 */
@Getter
public enum MsgDataType {
    BYTE(1, "无符号单字节整型(字节，8 位)", of(byte.class, Byte.class, Short.class, short.class, int.class, Integer.class, long.class, Long.class)),
    BYTES(0, "", of(byte[].class, Byte[].class, ByteBuf.class)),
    WORD(2, "无符号双字节整型(字，16 位)", of(short.class, Short.class, int.class, Integer.class, long.class, Long.class)),
    DWORD(4, "无符号四字节整型(双字，32 位)", of(long.class, Long.class, int.class, Integer.class)),
    BCD(0, "8421 码，n 字节", of(String.class)),
    STRING(0, "GBK 编码，若无数据，置空", of(String.class)),
    LIST(0, "List", of(List.class)),
    UNKNOWN(0, "未知类型，用于占位符或默认值", of(String.class)),
    ;

    /**
     * 字节数
     * 为零表示使用外部指定的长度
     *
     * @see RequestField#length()
     */
    private final int byteCount;

    private final String desc;

    private final Set<Class<?>> expectedTargetClassType;

    MsgDataType(int byteCount, String desc, Set<Class<?>> expectedTargetClassType) {
        this.byteCount = byteCount;
        this.desc = desc;
        this.expectedTargetClassType = expectedTargetClassType;
    }

}
