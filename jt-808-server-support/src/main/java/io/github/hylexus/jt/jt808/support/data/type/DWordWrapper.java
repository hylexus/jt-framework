package io.github.hylexus.jt.jt808.support.data.type;

import io.github.hylexus.jt.jt808.support.utils.BytesUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public class DWordWrapper implements BytesValueWrapper<Integer> {
    private Integer value;

    public DWordWrapper() {
    }

    public DWordWrapper(Integer value) {
        this.value = value;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        BytesUtils.writeWord(byteBuf, value);
    }

    @Override
    public Integer read(ByteBuf byteBuf, int offset, int length) {
        value = BytesUtils.readWord(byteBuf);
        return value;
    }

    @Override
    public Integer value() {
        return value;
    }
}
