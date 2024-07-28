package io.github.hylexus.jt.jt808.support.data.type;

import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
//@Deprecated(since = "2.1.1")
@Deprecated
public class BcdWrapper implements BytesValueWrapper<String> {
    private String value;

    public BcdWrapper() {
    }

    public BcdWrapper(String value) {
        this.value = value;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        JtProtocolUtils.writeBcd(byteBuf, value);
    }

    @Override
    public String read(ByteBuf byteBuf, int offset, int length) {
        value = JtProtocolUtils.readBcd(byteBuf, length);
        return value;
    }

    @Override
    public String value() {
        return value;
    }
}
