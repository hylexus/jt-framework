package io.github.hylexus.jt.jt808.support.annotation.codec;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hylexus
 */
@Getter
@AllArgsConstructor
public class AnnotationDecoderContext {
    /**
     * 当前正在迭代的数据大小(字节数)
     */
    private final int msgBodyLength;
    private final ByteBuf evaluationSource;

    public int msgBodyLength() {
        return msgBodyLength;
    }
}
