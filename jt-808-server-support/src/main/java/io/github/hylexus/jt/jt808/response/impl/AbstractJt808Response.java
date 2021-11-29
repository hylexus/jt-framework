package io.github.hylexus.jt.jt808.response.impl;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.jt808.response.Jt808Response;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.spec.Jt808MsgBodySpec;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808MsgBodySpec;
import io.github.hylexus.jt.jt808.support.codec.Jt808ByteBuf;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public class AbstractJt808Response implements Jt808Response {
    private MsgType msgType;
    private Jt808ProtocolVersion version;
    private String terminalId;
    private int flowId;
    private Jt808MsgBodySpec body;

    public AbstractJt808Response(MsgType msgType, ByteBuf body, Jt808Session session) {
        this(msgType, new DefaultJt808MsgBodySpec(Jt808ByteBuf.from(body)), session);
    }

    public AbstractJt808Response(MsgType msgType, Jt808MsgBodySpec body, Jt808Session session) {
        this(msgType, session.getProtocolVersion(), session.getTerminalId(), session.getCurrentFlowId(true), body);
    }

    public AbstractJt808Response(MsgType msgType, Jt808ProtocolVersion version, String terminalId, int flowId, ByteBuf body) {
        this(msgType, version, terminalId, flowId, new DefaultJt808MsgBodySpec(Jt808ByteBuf.from(body)));
    }

    public AbstractJt808Response(MsgType msgType, Jt808ProtocolVersion version, String terminalId, int flowId, Jt808MsgBodySpec body) {
        this.msgType = msgType;
        this.version = version;
        this.terminalId = terminalId;
        this.flowId = flowId;
        this.body = body;
    }

    @Override
    public int msgId() {
        return msgType.getMsgId();
    }

    @Override
    public Jt808ProtocolVersion version() {
        return version;
    }

    @Override
    public String terminalId() {
        return terminalId;
    }

    @Override
    public int flowId() {
        return flowId;
    }

    @Override
    public Jt808MsgBodySpec body() {
        return body;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public void setVersion(Jt808ProtocolVersion version) {
        this.version = version;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public void setFlowId(int flowId) {
        this.flowId = flowId;
    }

    public void setBody(Jt808MsgBodySpec body) {
        this.body = body;
    }
}
