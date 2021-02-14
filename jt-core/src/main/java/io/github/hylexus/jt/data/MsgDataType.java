package io.github.hylexus.jt.data;

import io.github.hylexus.jt.annotation.msg.req.basic.BasicField;
import io.github.hylexus.jt.annotation.msg.req.extra.ExtraField;
import lombok.Getter;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * @author hylexus
 * Created At 2019-09-18 8:50 下午
 */
@Getter
public enum MsgDataType {
    BYTE(1, "无符号单字节整型(字节，8 位)", newHashSet(byte.class, Byte.class, int.class, Integer.class, Short.class, short.class)),
    BYTES(0, "", newHashSet(byte[].class)),
    WORD(2, "无符号双字节整型(字，16 位)", newHashSet(short.class, Short.class, int.class, Integer.class)),
    // https://github.com/hylexus/jt-framework/issues/34
    DWORD(4, "无符号四字节整型(双字，32 位)", newHashSet(long.class, Long.class, int.class, Integer.class)),
    BCD(0, "8421 码，n 字节", newHashSet(String.class)),
    STRING(0, "GBK 编码，若无数据，置空", newHashSet(String.class)),
    /**
     * 目前只能用于 {@link ExtraField.NestedFieldMapping#dataType()} ,
     * 同时用 {@link ExtraField.NestedFieldMapping#itemDataType()} 指定List元素对应类型
     */
    LIST(0, "List", newHashSet(List.class)),
    UNKNOWN(0, "未知类型，用于占位符或默认值", newHashSet(String.class)),
    ;

    /**
     * 字节数
     * 为零表示使用外部指定的长度
     *
     * @see BasicField#length()
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
