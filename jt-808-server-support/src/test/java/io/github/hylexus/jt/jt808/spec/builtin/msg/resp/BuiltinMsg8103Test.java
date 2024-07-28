package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.builtin.msg.BaseReqRespMsgTest;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.support.data.type.bytebuf.ByteBufContainer;
import io.github.hylexus.jt.jt808.support.data.type.byteseq.ByteArrayContainer;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuiltinMsg8103Test extends BaseReqRespMsgTest {
    @Test
    void test() {
        final List<BuiltinMsg8103Alias.ParamItem> paramItemList = Jdk8Adapter.listOf(
                new BuiltinMsg8103Alias.ParamItem(0x0001, ByteBufContainer.ofDword(66)),
                new BuiltinMsg8103Alias.ParamItem(0x0013, ByteBufContainer.ofString("www.xxx.fff.zzz.com")),
                new BuiltinMsg8103Alias.ParamItem(0x0081, ByteBufContainer.ofWord(11)),
                new BuiltinMsg8103Alias.ParamItem(0x0084, ByteBufContainer.ofByte((byte) 1)),
                new BuiltinMsg8103Alias.ParamItem(0x0032, ByteBufContainer.ofBytes(new byte[]{0x16, 0x32, 0x0A, 0x1E}))
        );
        final BuiltinMsg8103Alias msg = new BuiltinMsg8103Alias()
                .setParamItemList(paramItemList)
                .setParamCount(paramItemList.size());

        final String hexString = encode(
                msg,
                builder -> builder.version(Jt808ProtocolVersion.VERSION_2013)
                        .terminalId(terminalId2013)
                        .msgId(BuiltinJt808MsgType.SERVER_SET_TERMINAL_PARAM)
        );

        paramItemList.forEach(it -> assertEquals(0, it.getMsgContent().value().refCnt()));

        assertEquals("7E8103003801391234432300000500000001040000004200000013137777772E7878782E6666662E7A7A7A2"
                + "E636F6D0000008102000B000000840101000000320416320A1EFE7E", hexString);
    }

    @Test
    void test1() {
        final List<BuiltinMsg8103.ParamItem> paramItemList = Jdk8Adapter.listOf(
                new BuiltinMsg8103.ParamItem(0x0001, ByteArrayContainer.ofDword(66)),
                new BuiltinMsg8103.ParamItem(0x0013, ByteArrayContainer.ofString("www.xxx.fff.zzz.com")),
                new BuiltinMsg8103.ParamItem(0x0081, ByteArrayContainer.ofWord(11)),
                new BuiltinMsg8103.ParamItem(0x0084, ByteArrayContainer.ofByte((byte) 1)),
                new BuiltinMsg8103.ParamItem(0x0032, ByteArrayContainer.ofBytes(new byte[]{0x16, 0x32, 0x0A, 0x1E}))
        );
        final BuiltinMsg8103 msg = new BuiltinMsg8103()
                .setParamItemList(paramItemList)
                .setParamCount(paramItemList.size());

        final String hexString = encode(
                msg,
                builder -> builder.version(Jt808ProtocolVersion.VERSION_2013)
                        .terminalId(terminalId2013)
                        .msgId(BuiltinJt808MsgType.SERVER_SET_TERMINAL_PARAM)
        );

        assertEquals("7E8103003801391234432300000500000001040000004200000013137777772E7878782E6666662E7A7A7A2"
                + "E636F6D0000008102000B000000840101000000320416320A1EFE7E", hexString);
    }
}
