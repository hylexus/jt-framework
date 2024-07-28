package io.github.hylexus.jt.jt808.spec.impl.msg.builder;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.Jt808MsgEncryptionHandler;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGenerator;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageStorage;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ByteBufJt808MsgBuilderTest {

    Jt808FlowIdGenerator flowIdGenerator = step -> 0;

    @Test
    void test1() {
        try (ByteBufJt808MsgBuilder builder = Jt808MsgBuilder.newByteBufBuilder(flowIdGenerator)) {
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
        }
    }

    @Test
    void test2() {
        final ByteBuf originalBuf = ByteBufAllocator.DEFAULT.buffer();
        try (ByteBufJt808MsgBuilder builder = Jt808MsgBuilder.newByteBufBuilder(flowIdGenerator, originalBuf)) {
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
        try (ByteBufJt808MsgBuilder builder = Jt808MsgBuilder.newByteBufBuilder(flowIdGenerator, encoder, originalBuf)) {
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
        try (ByteBufJt808MsgBuilder builder = Jt808MsgBuilder.newByteBufBuilder(flowIdGenerator, encoder)) {
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
        }
    }

    private void doSomeProcess(ByteBuf ignore) {
    }
}
