package io.github.hylexus.jt.msg.builder.jt808;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.utils.ProtocolUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

/**
 * Created At 2021/01/15 19:40
 *
 * @author hylexus
 */
@Getter
public class MsgHeaderSpec {

    // 1. byte[0-1]    消息ID word(16)
    private final int msgId;

    // 2. byte[2-3] 消息体属性 word(16)
    private final MsgBodyPropsSpec msgBodyPropsSpec;

    // 3. byte[4-9]    终端手机号或设备ID bcd[6]
    // --> 根据安装后终端自身的手机号转换
    // --> 手机号不足12 位，则在前面补 0
    private final String terminalId;

    // 4. byte[10-11]    消息流水号 word(16): 按发送顺序从 0 开始循环累加
    private final int flowId;

    // 5. byte[12-15]    消息包封装项
    // --> byte[0-1]     消息包总数(word(16)) : 该消息分包后得总包数
    // --> byte[2-3]     包序号(word(16)): 从 1 开始, 如果消息体属性中相关标识位确定消息分包处理,则该项有内容,否则无该项


    public MsgHeaderSpec(int msgId, MsgBodyPropsSpec msgBodyPropsSpec, String terminalId, int flowId) {
        this.msgId = msgId;
        this.msgBodyPropsSpec = msgBodyPropsSpec;
        this.terminalId = terminalId;
        this.flowId = flowId;
    }

    public byte[] toBytes() {
        int bodyProps = ProtocolUtils.generateMsgBodyPropsForJt808(
                msgBodyPropsSpec.getMsgBodyLength(),
                msgBodyPropsSpec.getEncryptionType(),
                msgBodyPropsSpec.isSubPackage(),
                msgBodyPropsSpec.getVersion(),
                msgBodyPropsSpec.reversedBit15
        );
        return ProtocolUtils.generateMsgHeaderForJt808RespMsg(msgId, bodyProps, msgBodyPropsSpec.getVersion(), terminalId, flowId);
    }

    // byte[2-3] 消息体属性 word(16)
    @Getter
    public static class MsgBodyPropsSpec {
        // bit[0-9] 消息体长度
        @Setter
        private int msgBodyLength;

        // bit[10-12] 数据加密方式
        // 此三位都为 0，表示消息体不加密
        // 第 10 位为 1，表示消息体经过 RSA 算法加密
        // 其它保留
        private final int encryptionType;

        // bit[13]  分包
        // 1：消息体为长消息，进行分包发送处理，具体分包信息由消息包封装项决定
        // 0：则消息头中无消息包封装项字段
        private final boolean isSubPackage;

        // bit[14]  版本号
        private final Jt808ProtocolVersion version;
        // bit[15]  保留
        private final int reversedBit15;

        public MsgBodyPropsSpec(int msgBodyLength, int encryptionType, boolean isSubPackage, Jt808ProtocolVersion version, int reversedBit15) {
            this.msgBodyLength = msgBodyLength;
            this.encryptionType = encryptionType;
            this.isSubPackage = isSubPackage;
            this.version = version;
            this.reversedBit15 = reversedBit15;
        }
    }

    public static class MsgBodyPropsSpecBuilder {
        // bit[0-9] 消息体长度
        private int msgBodyLength = -1;

        // bit[10-12] 数据加密方式
        private int encryptionType = 0b000;

        // bit[13]  分包
        // 1：消息体为长消息，进行分包发送处理，具体分包信息由消息包封装项决定
        // 0：则消息头中无消息包封装项字段
        private boolean isSubPackage = false;

        // bit[14]  版本号
        private Jt808ProtocolVersion version = Jt808ProtocolVersion.VERSION_2011;

        // bit[15]  保留
        private int reversedBit15 = 0;

        public static MsgBodyPropsSpecBuilder builder() {
            return new MsgBodyPropsSpecBuilder();
        }

        /**
         * @param msgBodyLength 消息体长度
         * @see Jt808MsgBuilder#build(java.util.function.Function, java.util.function.Function)
         * @deprecated 消息体长度会自动计算，该方法仅仅作为一个占位符
         */
        @Deprecated
        public MsgBodyPropsSpecBuilder withMsgBodyLength(int msgBodyLength) {
            this.msgBodyLength = msgBodyLength;
            return this;
        }

        public MsgBodyPropsSpecBuilder withEncryptionType(int encryptionType) {
            this.encryptionType = encryptionType;
            return this;
        }

        public MsgBodyPropsSpecBuilder withSubPackage(boolean isSubPackage) {
            this.isSubPackage = isSubPackage;
            return this;
        }

        public MsgBodyPropsSpecBuilder withVersion(Jt808ProtocolVersion version) {
            if (version == Jt808ProtocolVersion.AUTO_DETECTION) {
                throw new JtIllegalArgumentException("消息体属性第14位的值非法");
            }
            this.version = version;
            return this;
        }

        public MsgBodyPropsSpecBuilder withReversedBit15(int reversedBit15) {
            this.reversedBit15 = reversedBit15;
            return this;
        }

        public MsgBodyPropsSpec build() {
            return new MsgBodyPropsSpec(msgBodyLength, encryptionType, isSubPackage, version, reversedBit15);
        }

    }

    public static class MsgHeaderSpecBuilder {
        // 1. byte[0-1]    消息ID word(16)
        private int msgId;

        // 2. byte[2-3] 消息体属性 word(16)
        private MsgBodyPropsSpec msgBodyPropsSpec;

        // 3. byte[4-9] 终端手机号或设备ID bcd[6]
        private String terminalId;

        // 4. byte[10-11] 消息流水号 word(16): 按发送顺序从 0 开始循环累加
        private int flowId;

        // 5. byte[12-15]    消息包封装项
        // --> byte[0-1]     消息包总数(word(16)) : 该消息分包后得总包数
        // --> byte[2-3]     包序号(word(16)): 从 1 开始, 如果消息体属性中相关标识位确定消息分包处理,则该项有内容,否则无该项

        public MsgHeaderSpecBuilder withMsgId(MsgType msgType) {
            this.msgId = msgType.getMsgId();
            return this;
        }

        public MsgHeaderSpecBuilder withMsgId(int msgId) {
            this.msgId = msgId;
            return this;
        }

        public MsgHeaderSpecBuilder withMsgBodyPropsSpec(MsgBodyPropsSpec msgBodyPropsSpec) {
            this.msgBodyPropsSpec = msgBodyPropsSpec;
            return this;
        }

        public MsgHeaderSpecBuilder withMsgBodyPropsSpec(Function<MsgBodyPropsSpecBuilder, MsgBodyPropsSpec> builder) {
            this.msgBodyPropsSpec = builder.apply(new MsgBodyPropsSpecBuilder());
            return this;
        }

        public MsgHeaderSpecBuilder withTerminalId(String terminalId) {
            this.terminalId = terminalId;
            return this;
        }

        public MsgHeaderSpecBuilder withFlowId(int flowId) {
            this.flowId = flowId;
            return this;
        }

        public MsgHeaderSpec build() {
            return new MsgHeaderSpec(msgId, msgBodyPropsSpec, terminalId, flowId);
        }
    }
}
