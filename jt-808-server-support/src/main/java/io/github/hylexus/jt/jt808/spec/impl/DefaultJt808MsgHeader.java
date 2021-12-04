package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808MsgHeader;
import lombok.Setter;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author hylexus
 */
@Setter
public class DefaultJt808MsgHeader implements Jt808MsgHeader {

    private Jt808ProtocolVersion version;

    // byte[0-1] 消息ID
    private int msgId;

    // byte[2-3] 消息体属性
    private MsgBodyPropsSpec msgBodyPropsSpec;

    // byte[4-9] 终端Id
    private String terminalId;

    // byte[10-11] 流水号
    private int flowId;

    private SubPackageSpec subPackageSpec;

    @Override
    public Jt808ProtocolVersion version() {
        return version;
    }

    @Override
    public int msgType() {
        return msgId;
    }

    @Override
    public MsgBodyPropsSpec msgBodyProps() {
        return msgBodyPropsSpec;
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
    public Optional<SubPackageSpec> subPackageSpec() {
        return Optional.ofNullable(subPackageSpec);
    }

    public void setMsgBodyPropsField(int msgBodyPropsField) {
        this.msgBodyPropsSpec = new DefaultMsgBodyPropsSpec(msgBodyPropsField);
    }

    @Override
    public String toString() {
        return "HeaderSpec{"
               + "version=" + version
               + ", terminalId='" + terminalId + '\''
               + ", msgId=" + msgId
               + ", flowId=" + flowId
               + ", msgBodyProps=" + msgBodyPropsSpec
               + '}';
    }

    public static class DefaultJt808MsgHeaderSpecBuilder {
        private Jt808ProtocolVersion version;
        // byte[0-1] 消息ID
        private int msgId;

        // byte[2-3] 消息体属性
        private MsgBodyPropsSpec msgBodyPropsSpec;

        // byte[4-9] 终端Id
        private String terminalId;

        // byte[10-11] 流水号
        private int flowId;

        private SubPackageSpec subPackageSpec;

        public DefaultJt808MsgHeaderSpecBuilder withMsgId(int msgId) {
            this.msgId = msgId;
            return this;
        }

        public DefaultJt808MsgHeaderSpecBuilder withMsgBodyPropsSpec(MsgBodyPropsSpec msgBodyPropsSpec) {
            this.msgBodyPropsSpec = msgBodyPropsSpec;
            return this;
        }

        public DefaultJt808MsgHeaderSpecBuilder withMsgBodyPropsSpec(Consumer<DefaultMsgBodyPropsSpec.DefaultMsgBodyPropsSpecBuilder> builder) {
            final DefaultMsgBodyPropsSpec.DefaultMsgBodyPropsSpecBuilder specBuilder = new DefaultMsgBodyPropsSpec.DefaultMsgBodyPropsSpecBuilder();
            builder.accept(specBuilder);
            this.msgBodyPropsSpec = specBuilder.build();
            return this;
        }

        public DefaultJt808MsgHeaderSpecBuilder withTerminalId(String terminalId) {
            this.terminalId = terminalId;
            return this;
        }

        public DefaultJt808MsgHeaderSpecBuilder withFlowId(int flowId) {
            this.flowId = flowId;
            return this;
        }

        public DefaultJt808MsgHeaderSpecBuilder withSubPackageSpec(SubPackageSpec subPackageSpec) {
            this.subPackageSpec = subPackageSpec;
            return this;
        }

        public DefaultJt808MsgHeaderSpecBuilder withJt808ProtocolVersion(Jt808ProtocolVersion version) {
            this.version = version;
            return this;
        }

        public DefaultJt808MsgHeader build() {
            final DefaultJt808MsgHeader spec = new DefaultJt808MsgHeader();
            spec.setMsgId(msgId);
            spec.setMsgBodyPropsSpec(msgBodyPropsSpec);
            spec.setVersion(version);
            spec.setTerminalId(terminalId);
            spec.setFlowId(flowId);
            spec.setSubPackageSpec(subPackageSpec);
            return spec;
        }
    }
}
