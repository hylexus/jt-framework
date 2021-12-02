package io.github.hylexus.jt.jt808.support.data.type;

import io.github.hylexus.jt.jt808.support.utils.BytesUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public class BcdWrapper implements BytesValueWrapper<String> {
    private String value;

    public BcdWrapper() {
    }

    public BcdWrapper(String value) {
        this.value = value;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        BytesUtils.writeBcd(byteBuf, value);
    }

    @Override
    public String read(ByteBuf byteBuf, int offset, int length) {
        value = BytesUtils.readBcd(byteBuf, length);
        return value;
    }

    @Override
    public String value() {
        return value;
    }
}
