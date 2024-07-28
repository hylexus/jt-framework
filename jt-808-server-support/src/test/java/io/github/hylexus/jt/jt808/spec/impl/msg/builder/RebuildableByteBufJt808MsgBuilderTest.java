package io.github.hylexus.jt.jt808.spec.impl.msg.builder;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.Jt808MsgEncryptionHandler;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGenerator;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageStorage;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgEncoder;
import io.github.hylexus.jt.utils.FormatUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RebuildableByteBufJt808MsgBuilderTest {
    Jt808FlowIdGenerator flowIdGenerator = step -> 0;


    @Test
    void test1() {
        try (RebuildableByteBufJt808MsgBuilder builder = Jt808MsgBuilder.newRebuildableByteBufBuilder(flowIdGenerator)) {
            builder.version(Jt808ProtocolVersion.VERSION_2019)
                    .msgId(0x1111)
                    .terminalId("013912344323")
                    .body(writer -> writer.writeDWord(1)
                            .writeWord(2)
                            .writeString("abc")
                    );
            final ByteBuf result = builder.build();
            Assertions.assertEquals(1, result.refCnt());

            doSomeProcess(result);

            result.release();
            Assertions.assertEquals(0, result.refCnt());

            final ByteBuf result2 = builder.build();
            Assertions.assertEquals(1, result2.refCnt());

            doSomeProcess(result2);

            result2.release();
            Assertions.assertEquals(0, result2.refCnt());
        }
    }

    @Test
    void test2() {
        final ByteBuf originalBuf = ByteBufAllocator.DEFAULT.buffer();
        try (RebuildableByteBufJt808MsgBuilder builder = Jt808MsgBuilder.newRebuildableByteBufBuilder(flowIdGenerator, originalBuf)) {
            builder.version(Jt808ProtocolVersion.VERSION_2013)
                    .msgId(BuiltinJt808MsgType.CLIENT_COMMON_REPLY)
                    .terminalId("013912344323")
                    // 消息体借助 Jt808ByteWriter 来写入内容
                    // 也可以直接提供一个已经写好内容的 ByteBuf 用来充当消息体
                    .body(writer -> writer
                            // 1. 应答流水号 WORD    对应的平台消息的流水号
                            .writeWord(0)
                            // 2. 应答id WORD     对应的平台消息的 ID
                            .writeWord(0x8103)
                            // 3. 结果  byte 0:成功/确认;1:失败;2:消息有误;3:不支持
                            .writeByte(0)
                    );
            final ByteBuf result = builder.build();
            Assertions.assertEquals(1, result.refCnt());
            Assertions.assertEquals("7E0001000501391234432300000000810300F87E", FormatUtils.toHexString(result));

            doSomeProcess(result);

            result.release();
            Assertions.assertEquals(0, result.refCnt());

            final ByteBuf result2 = builder.build();
            Assertions.assertEquals(1, result2.refCnt());

            doSomeProcess(result2);

            result2.release();
            Assertions.assertEquals(0, result2.refCnt());
        }
        // try-with-resources 会自动释放 originalBuf
        Assertions.assertEquals(0, originalBuf.refCnt());
    }

    @Test
    void test3() {
        final Jt808MsgEncoder encoder = new DefaultJt808MsgEncoder(
                ByteBufAllocator.DEFAULT,
                new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT),
                responseSubPackage -> {
                },
                Jt808ResponseSubPackageStorage.NO_OPS_STORAGE,
                Jt808MsgEncryptionHandler.NO_OPS
        );
        final ByteBuf originalBuf = ByteBufAllocator.DEFAULT.buffer();
        try (RebuildableByteBufJt808MsgBuilder builder = Jt808MsgBuilder.newRebuildableByteBufBuilder(flowIdGenerator, encoder, originalBuf)) {
            builder.version(Jt808ProtocolVersion.VERSION_2019)
                    .msgId(0x1111)
                    .terminalId("013912344323")
                    .body(writer -> writer.writeDWord(1)
                            .writeWord(2)
                            .writeString("abc")
                    );
            final ByteBuf result = builder.build();
            Assertions.assertEquals(1, result.refCnt());

            doSomeProcess(result);

            result.release();
            Assertions.assertEquals(0, result.refCnt());

            final ByteBuf result2 = builder.build();
            Assertions.assertEquals(1, result2.refCnt());

            doSomeProcess(result2);

            result2.release();
            Assertions.assertEquals(0, result2.refCnt());
        }
        // try-with-resources 会自动释放 originalBuf
        Assertions.assertEquals(0, originalBuf.refCnt());
    }

    @Test
    void test4() {
        final Jt808MsgEncoder encoder = new DefaultJt808MsgEncoder(
                ByteBufAllocator.DEFAULT,
                new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT),
                responseSubPackage -> {
                },
                Jt808ResponseSubPackageStorage.NO_OPS_STORAGE,
                Jt808MsgEncryptionHandler.NO_OPS
        );
        try (RebuildableByteBufJt808MsgBuilder builder = Jt808MsgBuilder.newRebuildableByteBufBuilder(flowIdGenerator, encoder)) {
            builder.version(Jt808ProtocolVersion.VERSION_2019)
                    .msgId(0x1111)
                    .terminalId("013912344323")
                    .body(writer -> writer.writeDWord(1)
                            .writeWord(2)
                            .writeString("abc")
                    );
            final ByteBuf result = builder.build();
            Assertions.assertEquals(1, result.refCnt());

            doSomeProcess(result);

            result.release();
            Assertions.assertEquals(0, result.refCnt());

            final ByteBuf result2 = builder.build();
            Assertions.assertEquals(1, result2.refCnt());

            doSomeProcess(result2);

            result2.release();
            Assertions.assertEquals(0, result2.refCnt());
        }
    }

    private void doSomeProcess(ByteBuf ignore) {
    }
}
