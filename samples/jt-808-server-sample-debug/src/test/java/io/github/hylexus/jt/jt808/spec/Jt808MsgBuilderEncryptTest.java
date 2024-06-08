package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.samples.debug.handler.Jt808MsgEncryptionHandlerDemo01;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.spec.impl.msg.builder.EntityJt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.impl.msg.builder.RebuildableByteBufJt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGenerator;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseFieldAlias;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageStorage;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgEncoder;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author hylexus
 */
@Slf4j
public class Jt808MsgBuilderEncryptTest {

    // 这里使用-1，通过 Jt808MsgBuilder.msgId(int msgId) 来指定了消息ID
    // 也可以直接在这里指定具体的消息ID
    @Data
    @Accessors(chain = true)
    @Jt808ResponseBody(msgId = -1)
    static class MockEntityForEncrypt {
        // 1. [0-2) WORD 省域ID
        // WORD 类型固定长度就是2字节 所以无需指定length
        @ResponseFieldAlias.Word(order = 1)
        private int provinceId;

        // 2. [2-4) WORD 市县域ID
        @ResponseFieldAlias.Word(order = 2)
        private int cityId;

        // 3. [4-15) BYTE[11] 制造商ID
        @ResponseFieldAlias.Bytes(order = 3)
        private String manufacturerId;

        // 4. [15-45) BYTE[30] 终端型号
        @ResponseFieldAlias.Bytes(order = 4)
        private String terminalType;

        // 5. [45-75) BYTE[30] 终端ID
        @ResponseFieldAlias.Bytes(order = 5)
        private String terminalId;

        // 6. [75]   BYTE    车牌颜色
        @ResponseFieldAlias.Byte(order = 6)
        private byte color;

        // 7. [76,n)   String    车辆标识
        // 使用 SpEL 计算消息长度(上下文中的消息体总长度减去前面消费掉的字节数)
        @ResponseFieldAlias.String(order = 7)
        private String carIdentifier;
    }

    // 流水号生成器(这里使用一个永远返回1的生成器用来调试)
    // 可以使用 Jt808Session(已经实现了Jt808FlowIdGenerator) 或者 Jt808FlowIdGenerator.DEFAULT(默认实现类) 来生成自增的流水号
    private static final Jt808FlowIdGenerator ALWAYS_RETURN_1 = step -> 1;
    final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
    final DefaultJt808MsgEncoder encoder = new DefaultJt808MsgEncoder(
            allocator,
            new DefaultJt808MsgBytesProcessor(allocator),
            responseSubPackage -> {
            },
            Jt808ResponseSubPackageStorage.NO_OPS_STORAGE,
            new Jt808MsgEncryptionHandlerDemo01()
    );

    @Test
    void testEntityMsgBuilder() {
        // 通过实体类来转换消息体内容
        final MockEntityForEncrypt entity = new MockEntityForEncrypt()
                .setProvinceId(11)
                .setCityId(2)
                .setManufacturerId("id987654321")
                .setTerminalType("type00123456781234567887654321")
                .setTerminalId("ID0000123456781234567887654321")
                .setColor((byte) 1)
                .setCarIdentifier("甘J-123459");


        final EntityJt808MsgBuilder builder = Jt808MsgBuilder.newEntityBuilder(ALWAYS_RETURN_1, encoder)
                .version(Jt808ProtocolVersion.VERSION_2019)
                // .terminalId("00000000013912344329")
                .terminalId("00000000000000000666")
                .msgId(BuiltinJt808MsgType.CLIENT_REGISTER)
                .encryptionType(0b010)
                .body(entity);

        final ByteBuf result = builder.build();

        assertEquals("7E0100486001000000000000000006660001F54379B894A198DF648501D34446F71B043C33FBC988AA593AD96111482DD6DBBE8C573E551F2A2349D911536E9A033692A9C8F5A4DEC595738119DB95BECFA5FCCBFC9F4B1A5BA57FB43DDE36E28E6F7FAF5EA024C18DC3F2BEB4DCD247D4651D7E", HexStringUtils.byteBufToString(result));
        assertEquals("7E0100486001000000000000000006660001F54379B894A198DF648501D34446F71B043C33FBC988AA593AD96111482DD6DBBE8C573E551F2A2349D911536E9A033692A9C8F5A4DEC595738119DB95BECFA5FCCBFC9F4B1A5BA57FB43DDE36E28E6F7FAF5EA024C18DC3F2BEB4DCD247D4651D7E", builder.toHexString());

        assertEquals(1, result.refCnt());
        result.release();
        assertEquals(0, result.refCnt());
    }

    @Test
    public void testByteBufMsgBuilder() {
        // `./gradlew clean build -Dio.netty.allocator.type=pooled`
        // `./gradlew clean build -Dio.netty.allocator.type=unpooled`
        // final ByteBuf originalBuf = ByteBufAllocator.DEFAULT.buffer(128);
        final ByteBuf originalBuf = Unpooled.buffer(128);

        try (RebuildableByteBufJt808MsgBuilder builder = Jt808MsgBuilder.newRebuildableByteBufBuilder(ALWAYS_RETURN_1, encoder, originalBuf)) {

            builder.version(Jt808ProtocolVersion.VERSION_2013)
                    .msgId(BuiltinJt808MsgType.CLIENT_COMMON_REPLY)
                    .terminalId("013912344323")
                    .encryptionType(0b010)
                    // 消息体借助 Jt808ByteWriter 来写入内容
                    // 也可以直接提供一个已经写好内容的 ByteBuf 用来充当消息体
                    .body(writer -> writer
                            // 1. 应答流水号 WORD    对应的平台消息的流水号
                            .writeWord(0x1111)
                            // 2. 应答id WORD     对应的平台消息的 ID
                            .writeWord(0x8103)
                            // 3. 结果  byte 0:成功/确认;1:失败;2:消息有误;3:不支持
                            .writeByte(0x22)
                    );

            final ByteBuf result = builder.build();
            assertEquals("7E000108100139123443230001061EE8FEE8B9FA59FAF19642F93BC9E5AB7E", HexStringUtils.byteBufToString(result));
            assertEquals("7E000108100139123443230001061EE8FEE8B9FA59FAF19642F93BC9E5AB7E", builder.toHexString());

            assertEquals(1, originalBuf.refCnt());
            assertEquals(1, result.refCnt());

            // 在恰当的时机释放构建结果
            result.release();
            assertEquals(0, result.refCnt());
        }

        // try-with-resource 释放了 originalBuf
        assertEquals(0, originalBuf.refCnt());
    }
}
