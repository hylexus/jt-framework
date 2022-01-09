package io.github.hylexus.jt.jt808.spec.impl.response;

import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.netty.buffer.ByteBuf;

import java.time.LocalDateTime;

/**
 * @author hylexus
 */
public class DefaultJt808ResponseSubPackage implements Jt808Response.Jt808ResponseSubPackage {
    private final String terminalId;
    private final int msgId;
    private final int firstFlowIdOfPackageGroup;
    private final int flowId;
    private final int totalSubPackageCount;
    private final int currentPackageNo;
    private final ByteBuf msg;
    private final LocalDateTime createdAt;

    public DefaultJt808ResponseSubPackage(
            String terminalId, int msgId, int firstFlowIdOfPackageGroup, int flowId, int totalSubPackageCount, int currentPackageNo,
            ByteBuf msg, LocalDateTime createdAt) {

        this.terminalId = terminalId;
        this.msgId = msgId;
        this.firstFlowIdOfPackageGroup = firstFlowIdOfPackageGroup;
        this.flowId = flowId;
        this.totalSubPackageCount = totalSubPackageCount;
        this.currentPackageNo = currentPackageNo;
        this.msg = msg;
        this.createdAt = createdAt;
    }

    @Override
    public int firstFlowIdOfSubPackageGroup() {
        return this.firstFlowIdOfPackageGroup;
    }

    @Override
    public String terminalId() {
        return this.terminalId;
    }

    @Override
    public int msgId() {
        return this.msgId;
    }

    @Override
    public int flowId() {
        return this.flowId;
    }

    @Override
    public int totalSubPackageCount() {
        return this.totalSubPackageCount;
    }

    @Override
    public int currentPackageNo() {
        return this.currentPackageNo;
    }

    @Override
    public ByteBuf msg() {
        return this.msg;
    }

    @Override
    public LocalDateTime createdAt() {
        return this.createdAt;
    }

    @Override
    public Jt808Response.Jt808ResponseSubPackage copy() {
        return new DefaultJt808ResponseSubPackage(
                this.terminalId, this.msgId, this.firstFlowIdOfPackageGroup, this.flowId, this.totalSubPackageCount, this.currentPackageNo,
                this.msg.copy(),
                this.createdAt
        );
    }
}
