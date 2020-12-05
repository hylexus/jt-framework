package io.github.hylexus.jt.utils;

import com.google.common.collect.Lists;
import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.oaks.utils.Bytes;
import lombok.Data;

import java.util.function.Function;

/**
 * Created At 2020/12/5 2:35 下午
 *
 * @author hylexus
 */
public class Jt808MessageBuilder {
    @Data
    static class HeaderSpec {
        // 1. byte[0-1]    消息ID word(16)
        private int msgId;
        // 2. byte[2-3]    消息体属性 word(16)
        //private int msgBodyProps;
        // bit[10-12]    数据加密方式
        //    此三位都为 0，表示消息体不加密
        //    第 10 位为 1，表示消息体经过 RSA 算法加密
        //    其它保留
        private int encryptionType = 0b000;
        // 3. byte[4-9]    终端手机号或设备ID bcd[6]
        private String terminalId;
        // 4. byte[10-11]    消息流水号 word(16)
        private int flowId;

        public byte[] toBytes(int bodyLength) {
            int bodyProps = ProtocolUtils.generateMsgBodyPropsForJt808(bodyLength, encryptionType, false, 0);
            return ProtocolUtils.generateMsgHeaderForJt808RespMsg(msgId, bodyProps, terminalId, flowId);
        }
    }

    private final MessageBuilder messageBuilder;
    private final HeaderSpec headerSpec;

    private final Function<byte[], Byte> checksumCalculator;
    private final Function<byte[], byte[]> escapeFunction;

    private Jt808MessageBuilder(MessageBuilder messageBuilder, Function<byte[], Byte> checksumCalculator, Function<byte[], byte[]> escapeFunction) {
        this.messageBuilder = messageBuilder;
        this.headerSpec = new HeaderSpec();
        this.checksumCalculator = checksumCalculator;
        this.escapeFunction = escapeFunction;
    }

    public static Jt808MessageBuilder newBuilder(
            MessageBuilder messageBuilder,
            Function<byte[], Byte> checksumCalculator,
            Function<byte[], byte[]> escapeFunction) {
        return new Jt808MessageBuilder(messageBuilder, checksumCalculator, escapeFunction);
    }

    public Jt808MessageBuilder withMsgType(MsgType msgType) {
        headerSpec.setMsgId(msgType.getMsgId());
        return this;
    }

    public Jt808MessageBuilder withTerminalId(String terminalId) {
        headerSpec.setTerminalId(terminalId);
        return this;
    }

    public Jt808MessageBuilder withFlowId(int flowId) {
        headerSpec.setFlowId(flowId);
        return this;
    }

    public Jt808MessageBuilder encryptionType(int encryptionType) {
        headerSpec.setEncryptionType(encryptionType);
        return this;
    }

    public byte[] build() {
        return build(true);
    }

    public byte[] build(boolean escape) {
        final byte[] body = messageBuilder.build(false);
        final byte[] header = headerSpec.toBytes(body.length);
        byte[] headerAndBody = Bytes.concatAll(Lists.newArrayList(header, body));
        byte checkSum = this.checksumCalculator.apply(headerAndBody);
        return doEncode(headerAndBody, checkSum, escape);
    }

    private byte[] doEncode(byte[] headerAndBody, byte checkSum, boolean escape) {
        if (!escape) {
            return Bytes.concatAll(Lists.newArrayList(//
                    new byte[]{JtProtocolConstant.PACKAGE_DELIMITER}, // 0x7e
                    headerAndBody, // 消息头+ 消息体
                    new byte[]{checkSum},// 校验码
                    new byte[]{JtProtocolConstant.PACKAGE_DELIMITER}// 0x7e
            ));
        }
        final byte[] unescapedBytes = Bytes.concatAll(Lists.newArrayList(//
                headerAndBody, // 消息头+ 消息体
                new byte[]{checkSum}// 校验码
        ));

        final byte[] escaped = this.escapeFunction.apply(unescapedBytes);
        return Bytes.concatAll(
                new byte[]{JtProtocolConstant.PACKAGE_DELIMITER}, // 0x7e
                escaped,
                new byte[]{JtProtocolConstant.PACKAGE_DELIMITER} // 0x7e
        );
        // return this.escapeFunction.doEscapeForSend(unescapedBytes, 1, unescapedBytes.length - 2);
    }

}
