package io.github.hylexus.jt.msg.builder.jt808;

import com.google.common.collect.Lists;
import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.utils.Assertions;
import io.github.hylexus.jt.utils.ProtocolUtils;
import io.github.hylexus.oaks.utils.Bytes;

import java.util.function.Function;

/**
 * Created At 2021/01/15 19:35
 *
 * @author hylexus
 */
public class Jt808MsgBuilder {

    private static final Function<byte[], Byte> DEFAULT_CHECK_SUM_CALCULATOR = bytes -> ProtocolUtils.calculateCheckSum4Jt808(bytes, 0, bytes.length);
    private static final Function<byte[], byte[]> DEFAULT_ESCAPE_FUNCTION = bytes -> ProtocolUtils.doEscape4SendJt808Msg(bytes, 0, bytes.length - 1);

    private MsgHeaderSpec headerSpec;
    private byte[] body;

    public static Jt808MsgBuilder builder() {
        return new Jt808MsgBuilder();
    }

    public Jt808MsgBuilder header(MsgHeaderSpec headerSpec) {
        this.headerSpec = headerSpec;
        return this;
    }

    public Jt808MsgBuilder header(Function<MsgHeaderSpec.MsgHeaderSpecBuilder, MsgHeaderSpec> builder) {
        this.headerSpec = builder.apply(new MsgHeaderSpec.MsgHeaderSpecBuilder());
        return this;
    }

    public Jt808MsgBuilder body(byte[] body) {
        this.body = body;
        return this;
    }

    public Jt808MsgBuilder body(Function<Jt808MsgBodyBuilder, byte[]> bodyBuilderFunction) {
        this.body = bodyBuilderFunction.apply(Jt808MsgBodyBuilder.newBuilder());
        return this;
    }

    public byte[] build() {
        return build(true);
    }

    public byte[] build(boolean escape) {
        if (escape) {
            return build(DEFAULT_CHECK_SUM_CALCULATOR, DEFAULT_ESCAPE_FUNCTION);
        }

        return build(DEFAULT_CHECK_SUM_CALCULATOR, null);
    }

    public byte[] build(Function<byte[], Byte> checksumCalculator, Function<byte[], byte[]> escapeFunction) {

        Assertions.notNull(headerSpec, "[headerSpec] have not been set");
        Assertions.notNull(body, "[body] have not been set");
        Assertions.notNull(headerSpec.getMsgBodyPropsSpec(), "[header.msgBodyProps] have not been set");

        headerSpec.getMsgBodyPropsSpec().setMsgBodyLength(body.length);
        final byte[] header = headerSpec.toBytes();
        byte[] headerAndBody = Bytes.concatAll(Lists.newArrayList(header, body));
        byte checkSum = checksumCalculator.apply(headerAndBody);
        return doEncode(headerAndBody, checkSum, escapeFunction);
    }

    private byte[] doEncode(byte[] headerAndBody, byte checkSum, Function<byte[], byte[]> escapeFunction) {
        if (escapeFunction == null) {
            return Bytes.concatAll(Lists.newArrayList(//
                    new byte[]{JtProtocolConstant.PACKAGE_DELIMITER}, // 0x7e
                    headerAndBody, // 消息头+ 消息体
                    new byte[]{checkSum},// 校验码
                    new byte[]{JtProtocolConstant.PACKAGE_DELIMITER}// 0x7e
            ));
        }
        final byte[] unescapedBytes = Bytes.concatAll(Lists.newArrayList(
                headerAndBody, // 消息头+ 消息体
                new byte[]{checkSum}// 校验码
        ));

        final byte[] escaped = escapeFunction.apply(unescapedBytes);
        return Bytes.concatAll(
                new byte[]{JtProtocolConstant.PACKAGE_DELIMITER}, // 0x7e
                escaped,
                new byte[]{JtProtocolConstant.PACKAGE_DELIMITER} // 0x7e
        );
    }

}
