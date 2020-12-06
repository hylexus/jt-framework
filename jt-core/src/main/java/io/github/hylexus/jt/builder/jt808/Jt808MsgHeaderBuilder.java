package io.github.hylexus.jt.builder.jt808;

import io.github.hylexus.jt.data.msg.MsgType;

public class Jt808MsgHeaderBuilder {

    private final Jt808MsgHeaderSpec headerSpec;

    public Jt808MsgHeaderBuilder() {
        this.headerSpec = new Jt808MsgHeaderSpec();
    }

    public static Jt808MsgHeaderBuilder newBuilder() {
        return new Jt808MsgHeaderBuilder();
    }

    public Jt808MsgHeaderBuilder withTerminalId(String terminalId) {
        headerSpec.setTerminalId(terminalId);
        return this;
    }

    public Jt808MsgHeaderBuilder withMsgId(int msgId) {
        headerSpec.setMsgId(msgId);
        return this;
    }

    public Jt808MsgHeaderBuilder withMsgId(MsgType msgType) {
        headerSpec.setMsgId(msgType.getMsgId());
        return this;
    }

    public Jt808MsgHeaderBuilder withFlowId(int flowId) {
        headerSpec.setFlowId(flowId);
        return this;
    }

    public Jt808MsgHeaderBuilder withEncryptionType(int encryptionType) {
        headerSpec.setEncryptionType(encryptionType);
        return this;
    }

    public Jt808MsgHeaderSpec build() {
        return this.headerSpec;
    }

}