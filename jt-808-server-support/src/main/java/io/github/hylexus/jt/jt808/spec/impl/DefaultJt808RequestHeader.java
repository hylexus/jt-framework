package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808RequestHeader;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 */
@Accessors(fluent = true, chain = true)
@Setter
public class DefaultJt808RequestHeader implements Jt808RequestHeader {

    private Jt808ProtocolVersion version;

    // byte[0-1] 消息ID
    private int msgType;

    // byte[2-3] 消息体属性
    private Jt808MsgBodyProps msgBodyProps;

    // byte[4-9] 终端Id
    private String terminalId;

    // byte[10-11] 流水号
    private int flowId;

    private Jt808SubPackageProps subPackageProps;

    public DefaultJt808RequestHeader() {
    }

    public DefaultJt808RequestHeader(
            Jt808ProtocolVersion version, int msgType, Jt808MsgBodyProps msgBodyProps, String terminalId, int flowId, Jt808SubPackageProps subPackageProps) {
        this.version = version;
        this.msgType = msgType;
        this.msgBodyProps = msgBodyProps;
        this.terminalId = terminalId;
        this.flowId = flowId;
    }

    @Override
    public Jt808ProtocolVersion version() {
        return version;
    }

    @Override
    public int msgId() {
        return msgType;
    }

    @Override
    public Jt808MsgBodyProps msgBodyProps() {
        return msgBodyProps;
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
    public Jt808SubPackageProps subPackage() {
        return this.subPackageProps;
    }

    public void setMsgBodyPropsField(int msgBodyPropsField) {
        this.msgBodyProps = new DefaultJt808MsgBodyProps(msgBodyPropsField);
    }

    @Override
    public String toString() {
        return "HeaderSpec{"
               + "version=" + version
               + ", terminalId='" + terminalId + '\''
               + ", msgId=" + msgType
               + ", flowId=" + flowId
               + ", msgBodyProps=" + msgBodyProps
               + '}';
    }

    public static class DefaultJt808MsgHeaderBuilder implements Jt808MsgHeaderBuilder {

        private Jt808ProtocolVersion version;
        private int msgId;
        private Jt808MsgBodyProps msgBodyProps;
        private String terminalId;
        private int flowId;
        private Jt808SubPackageProps subPackageProps;

        public DefaultJt808MsgHeaderBuilder() {
        }

        public DefaultJt808MsgHeaderBuilder(Jt808RequestHeader header) {
            this.msgId = header.msgId();
            this.msgBodyProps = header.msgBodyProps();
            this.terminalId = header.terminalId();
            this.flowId = header.flowId();
            this.version = header.version();
        }

        @Override
        public Jt808MsgHeaderBuilder version(Jt808ProtocolVersion version) {
            this.version = version;
            return this;
        }

        @Override
        public Jt808MsgHeaderBuilder msgType(int msgType) {
            this.msgId = msgType;
            return this;
        }

        @Override
        public Jt808MsgHeaderBuilder msgBodyProps(Jt808MsgBodyProps msgBodyProps) {
            this.msgBodyProps = msgBodyProps;
            return this;
        }

        @Override
        public Jt808MsgHeaderBuilder terminalId(String terminalId) {
            this.terminalId = terminalId;
            return this;
        }

        @Override
        public Jt808MsgHeaderBuilder flowId(int flowId) {
            this.flowId = flowId;
            return this;
        }

        @Override
        public Jt808MsgHeaderBuilder subPackageProps(Jt808SubPackageProps subPackageProps) {
            this.subPackageProps = subPackageProps;
            return this;
        }

        @Override
        public Jt808RequestHeader build() {
            return new DefaultJt808RequestHeader(version, msgId, msgBodyProps, terminalId, flowId, subPackageProps);
        }
    }

}
