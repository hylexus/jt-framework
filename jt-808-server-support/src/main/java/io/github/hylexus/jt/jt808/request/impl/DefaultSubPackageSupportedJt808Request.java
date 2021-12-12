package io.github.hylexus.jt.jt808.request.impl;

import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.jt808.request.SubPackageSupportedJt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808MsgHeader;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public class DefaultSubPackageSupportedJt808Request
        extends DefaultJt808Request
        implements SubPackageSupportedJt808Request {

    private final Jt808SubPackage subPackage;

    public DefaultSubPackageSupportedJt808Request(
            MsgType msgType, Jt808MsgHeader header, ByteBuf rawByteBuf,
            ByteBuf body, byte originalCheckSum, byte calculatedCheckSum, Jt808SubPackage subPackageSpec) {
        super(msgType, header, rawByteBuf, body, originalCheckSum, calculatedCheckSum);
        this.subPackage = subPackageSpec;
    }

    @Override
    public Jt808SubPackage subPackage() {
        return subPackage;
    }
}
