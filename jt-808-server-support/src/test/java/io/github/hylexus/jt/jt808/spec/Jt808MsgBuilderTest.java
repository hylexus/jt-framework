package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.spec.impl.msg.builder.ByteBufJt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.impl.msg.builder.EntityJt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGenerator;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.BYTE;
import static io.github.hylexus.jt.jt808.support.data.MsgDataType.WORD;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author hylexus
 */
public class Jt808MsgBuilderTest {

    @Jt808ResponseBody(msgId = -1)
    @Data
    @Accessors(chain = true)
    static class TestEntity {
        // 1. 应答流水号 WORD    对应的平台消息的流水号
        @ResponseField(order = 0, dataType = WORD)
        int serverFlowId;

        // 2. 应答id WORD     对应的平台消息的 ID
        @ResponseField(order = 1, dataType = WORD)
        int serverMsgId;

        // 3. 结果  byte 0:成功/确认;1:失败;2:消息有误;3:不支持
        @ResponseField(order = 2, dataType = BYTE)
        int result;

    }

    private static final Jt808FlowIdGenerator ALWAYS_RETURN_0 = step -> 0;

    @Test
    void testEntityMsgBuilder() {
        final TestEntity entity = new TestEntity()
                .setServerFlowId(0)
                .setServerMsgId(0x8103)
                .setResult(0);
        final EntityJt808MsgBuilder builder = Jt808MsgBuilder.newEntityBuilder(ALWAYS_RETURN_0)
                .version(Jt808ProtocolVersion.VERSION_2013)
                .terminalId("013912344323")
                .body(entity)
                .msgId(BuiltinJt808MsgType.CLIENT_COMMON_REPLY);

        final ByteBuf result = builder.build();

        assertEquals("7E0001000501391234432300000000810300F87E", HexStringUtils.byteBufToString(result));
        assertEquals("7E0001000501391234432300000000810300F87E", builder.toHexString());

        assertEquals(1, result.refCnt());
        result.release();
        assertEquals(0, result.refCnt());
    }

    @Test
    public void testByteBufMsgBuilder() {
        final ByteBuf originalBuf = ByteBufAllocator.DEFAULT.buffer(128);
        final ByteBufJt808MsgBuilder builder = Jt808MsgBuilder.newByteBufBuilder(ALWAYS_RETURN_0, originalBuf)
                .version(Jt808ProtocolVersion.VERSION_2013)
                .msgId(BuiltinJt808MsgType.CLIENT_COMMON_REPLY)
                .terminalId("013912344323")
                .body(writer -> writer
                        // 1. 应答流水号 WORD    对应的平台消息的流水号
                        .writeWord(0)
                        // 2. 应答id WORD     对应的平台消息的 ID
                        .writeWord(0x8103)
                        // 3. 结果  byte 0:成功/确认;1:失败;2:消息有误;3:不支持
                        .writeByte(0)
                );

        final ByteBuf result = builder.build();
        assertEquals("7E0001000501391234432300000000810300F87E", HexStringUtils.byteBufToString(result));
        assertEquals("7E0001000501391234432300000000810300F87E", builder.toHexString());

        assertEquals(1, originalBuf.refCnt());
        assertEquals(result.refCnt(), originalBuf.refCnt());

        result.release();

        assertEquals(0, originalBuf.refCnt());
        assertEquals(result.refCnt(), originalBuf.refCnt());
    }
}