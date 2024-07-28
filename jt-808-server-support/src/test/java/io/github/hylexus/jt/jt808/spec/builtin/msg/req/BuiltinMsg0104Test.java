package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.JtProtocolConstant;
import io.github.hylexus.jt.jt808.spec.Jt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.Jt808MsgEncryptionHandler;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ProtocolVersionDetector;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ProtocolVersionDetectorRegistry;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.data.deserialize.DefaultJt808FieldDeserializerRegistry;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.github.hylexus.oaks.utils.IntBitOps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.*;
import static io.github.hylexus.jt.utils.HexStringUtils.byteBufToString;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author hylexus
 */
class BuiltinMsg0104Test {
    private final Jt808MsgDecoder jt808MsgDecoder = new DefaultJt808MsgDecoder(
            new BuiltinJt808MsgTypeParser(),
            new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT),
            new DefaultJt808ProtocolVersionDetectorRegistry(new DefaultJt808ProtocolVersionDetector()),
            Jt808MsgEncryptionHandler.NO_OPS
    );
    private final Jt808AnnotationBasedDecoder annotationBasedDecoder = new Jt808AnnotationBasedDecoder(new DefaultJt808FieldDeserializerRegistry(true));

    @Test
    void codecTest() {
        final Msg0104EncodePlaceholder placeholder = Msg0104EncodePlaceholder.builder()
                .serverFlowId(111)
                .count(2)
                .paramList(
                        Jdk8Adapter.listOf(
                                Msg0104EncodePlaceholder.TerminalParam.builder()
                                        .paramId(0x0001).contentLength(4).paramContent(IntBitOps.intTo4Bytes(99))
                                        .build(),
                                Msg0104EncodePlaceholder.TerminalParam.builder()
                                        .paramId(0x0012).contentLength(4).paramContent("abcd".getBytes(JtProtocolConstant.JT_808_STRING_ENCODING))
                                        .build()
                        )
                )
                .build();

        final String terminalId = "0000000000123456789";
        final ByteBuf byteBuf = Jt808MsgBuilder.newEntityBuilder(step -> 100)
                .version(Jt808ProtocolVersion.VERSION_2019)
                .terminalId(terminalId)
                .body(placeholder)
                .build();

        assertEquals("7E0104401501000000000001234567890064006F02000000010400000063000000120461626364A57E", byteBufToString(byteBuf));

        final Jt808Request request = jt808MsgDecoder.decode(byteBuf.slice(1, byteBuf.readableBytes() - 1));
        assertEquals(Jt808ProtocolVersion.VERSION_2019, request.version());
        assertEquals(0x0104, request.msgType().getMsgId());

        final BuiltinMsg0104 msg0104 = this.annotationBasedDecoder.decode(request, BuiltinMsg0104.class);
        assertEquals(placeholder.getServerFlowId(), msg0104.getServerFlowId());
        assertEquals(placeholder.getCount(), msg0104.getCount());
        assertEquals(placeholder.getParamList().size(), msg0104.getParamList().size());
        assertEquals(placeholder.getParamList().get(0).getParamId(), msg0104.getParamList().get(0).getParamId());
        assertArrayEquals(placeholder.getParamList().get(1).getParamContent(), msg0104.getParamList().get(1).getParamContent());

        // retained by DefaultJt808MsgDecoder.decode
        request.release();
        // self
        byteBuf.release();
        assertEquals(0, byteBuf.refCnt());
    }

    @Data
    @Accessors(chain = true)
    @Builder
    @Jt808ResponseBody(msgId = 0x0104)
    static class Msg0104EncodePlaceholder {
        /**
         * 1. 流水号 WORD
         */
        @ResponseField(order = 0, dataType = WORD)
        int serverFlowId;

        /**
         * 2. 参数个数
         */
        @ResponseField(order = 1, dataType = BYTE)
        int count;

        /**
         * 3. 参数项列表
         */
        @ResponseField(
                order = 2,
                dataType = LIST
        )
        List<Msg0104EncodePlaceholder.TerminalParam> paramList;

        @Data
        @Accessors(chain = true)
        @Builder
        public static class TerminalParam {

            // 参数id DWORD[4]
            @ResponseField(order = 0, dataType = DWORD)
            private int paramId;

            // 参数长度 BYTE[1]
            @ResponseField(order = 1, dataType = BYTE)
            private int contentLength;

            // 参数值
            @ResponseField(
                    order = 2,
                    dataType = BYTES
            )
            private byte[] paramContent;
        }
    }
}
