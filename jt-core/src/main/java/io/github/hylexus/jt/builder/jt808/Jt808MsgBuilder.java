package io.github.hylexus.jt.builder.jt808;

import com.google.common.collect.Lists;
import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.utils.ProtocolUtils;
import io.github.hylexus.oaks.utils.Bytes;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created At 2020/12/5 2:35 下午
 *
 * @author hylexus
 */
public class Jt808MsgBuilder {

    private Jt808MsgHeaderSpec headerSpec;
    private byte[] body;
    private static final Function<byte[], Byte> DEFAULT_CHECK_SUM_CALCULATOR = bytes -> ProtocolUtils.calculateCheckSum4Jt808(bytes, 0, bytes.length);
    private static final Function<byte[], byte[]> DEFAULT_ESCAPE_FUNCTION = bytes -> ProtocolUtils.doEscape4SendJt808Msg(bytes, 0, bytes.length - 1);

    public Jt808MsgBuilder() {
        this.headerSpec = new Jt808MsgHeaderSpec();
    }

    public static Jt808MsgBuilder newBuilder() {
        return new Jt808MsgBuilder();
    }

    public Jt808MsgBuilder header(Jt808MsgHeaderSpec headerSpec) {
        this.headerSpec = headerSpec;
        return this;
    }

    public Jt808MsgBuilder header(Consumer<Jt808MsgHeaderBuilder> headerBuilderConsumer) {
        final Jt808MsgHeaderBuilder headerBuilder = Jt808MsgHeaderBuilder.newBuilder();
        headerBuilderConsumer.accept(headerBuilder);
        return header(headerBuilder.build());
    }

    public Jt808MsgBuilder body(byte[] body) {
        this.body = body;
        return this;
    }

    public Jt808MsgBuilder body(Consumer<Jt808MsgBodyBuilder> msgBodyBuilderConsumer) {
        final Jt808MsgBodyBuilder msgHeaderBuilder = Jt808MsgBodyBuilder.newBuilder();
        msgBodyBuilderConsumer.accept(msgHeaderBuilder);
        return body(msgHeaderBuilder.build());
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
        final byte[] header = headerSpec.toBytes(body.length);
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
        final byte[] unescapedBytes = Bytes.concatAll(Lists.newArrayList(//
                headerAndBody, // 消息头+ 消息体
                new byte[]{checkSum}// 校验码
        ));

        final byte[] escaped = escapeFunction.apply(unescapedBytes);
        return Bytes.concatAll(
                new byte[]{JtProtocolConstant.PACKAGE_DELIMITER}, // 0x7e
                escaped,
                new byte[]{JtProtocolConstant.PACKAGE_DELIMITER} // 0x7e
        );
        // return this.escapeFunction.doEscapeForSend(unescapedBytes, 1, unescapedBytes.length - 2);
    }

}
