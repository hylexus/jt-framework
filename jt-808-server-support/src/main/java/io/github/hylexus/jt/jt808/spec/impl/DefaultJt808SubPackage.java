package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.jt808.spec.Jt808SubPackageRequest;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;

import java.time.LocalDateTime;

/**
 * @author hylexus
 */
public class DefaultJt808SubPackage implements Jt808SubPackageRequest.Jt808SubPackage {
    private final String terminalId;
    private final int msgId;
    private final int flowId;
    private final int totalSubPackageCount;
    private final int currentPackageNo;
    private final ByteBuf body;
    private final LocalDateTime createdAt;

    public DefaultJt808SubPackage(
            String terminalId, int msgId, int flowId, int totalSubPackageCount, int currentPackageNo,
            ByteBuf body, LocalDateTime createdAt) {

        this.terminalId = terminalId;
        this.msgId = msgId;
        this.flowId = flowId;
        this.totalSubPackageCount = totalSubPackageCount;
        this.currentPackageNo = currentPackageNo;
        this.body = body;
        this.createdAt = createdAt;
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
    public int flowId() {
        return flowId;
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

    @Override
    public LocalDateTime createdAt() {
        return createdAt;
    }
}
