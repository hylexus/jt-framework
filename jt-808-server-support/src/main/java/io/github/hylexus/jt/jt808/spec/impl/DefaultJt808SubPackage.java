package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.jt808.spec.Jt808SubPackageRequest;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public class DefaultJt808SubPackage implements Jt808SubPackageRequest.Jt808SubPackage {
    private final String terminalId;
    private final int msgId;
    private final int totalSubPackageCount;
    private final int currentPackageNo;
    private final ByteBuf body;

    public DefaultJt808SubPackage(String terminalId, int msgId, int totalSubPackageCount, int currentPackageNo, ByteBuf body) {
        this.terminalId = terminalId;
        this.msgId = msgId;
        this.totalSubPackageCount = totalSubPackageCount;
        this.currentPackageNo = currentPackageNo;
        this.body = body;
    }

    @Override
    public String terminalId() {
        return terminalId;
    }

    @Override
    public int msgId() {
        return msgId;
    }

    @Override
    public int totalSubPackageCount() {
        return totalSubPackageCount;
    }

    @Override
    public int currentPackageNo() {
        return currentPackageNo;
    }

    @Override
    public ByteBuf body() {
        return body;
    }

    @Override
    public String toString() {
        return "{" + terminalId + "-"
               + msgId + "(" + HexStringUtils.int2HexString(msgId, 4) + ") "
               + "(" + currentPackageNo + "/" + totalSubPackageCount + ")}";
    }
}
