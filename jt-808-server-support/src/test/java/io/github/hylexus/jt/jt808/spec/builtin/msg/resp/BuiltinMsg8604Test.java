package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.JtProtocolConstant;
import io.github.hylexus.jt.jt808.spec.builtin.msg.BaseReqRespMsgTest;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.github.hylexus.oaks.utils.Numbers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuiltinMsg8604Test extends BaseReqRespMsgTest {

    @Test
    void testBuild0x8604V2011() throws ParseException {
        final List<BuiltinMsg8604V2011Alias.Vertices> areaList = Jdk8Adapter.listOf(
                new BuiltinMsg8604V2011Alias.Vertices(31235930, 121480540),
                new BuiltinMsg8604V2011Alias.Vertices(31198320, 121547960)
        );
        final BuiltinMsg8604V2011Alias msg = new BuiltinMsg8604V2011Alias()
                .setAreaId(100)
                .setAreaAttr(Numbers.setBitAt(0, 1))
                .setStartTime(LocalDateTime.of(2023, 4, 17, 22, 16, 33))
                .setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2023-04-18 22:22:12"))
                .setMaxSpeed(77)
                .setContinuousTime((byte) 111)
                .setVerticesCount(areaList.size())
                .setVertices(areaList);

        String hexString = this.encode(msg, builder ->
                builder.version(Jt808ProtocolVersion.VERSION_2013)
                        .terminalId("013912344323")
                        .msgId(BuiltinJt808MsgType.SERVER_SET_POLYGON_AREA)
        );

        Assertions.assertEquals("7E8604001B0139123443230000000000640002004D6F000201DC9F5A073DA55C01DC0C70073EACB8F67E", hexString);
    }

    @Test
    void testBuild0x8604V2019() throws ParseException {
        final List<BuiltinMsg8604V2019Alias.Vertices> areaList = Jdk8Adapter.listOf(
                new BuiltinMsg8604V2019Alias.Vertices(31235930, 121480540),
                new BuiltinMsg8604V2019Alias.Vertices(31198320, 121547960)
        );
        final String areaName = "区域名称abcdefg123";
        final BuiltinMsg8604V2019Alias msg = new BuiltinMsg8604V2019Alias()
                .setAreaId(100)
                .setAreaAttr(Numbers.setBitAt(0, 1))
                .setStartTime(LocalDateTime.of(2023, 4, 17, 22, 16, 33))
                .setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2023-04-18 22:22:12"))
                .setMaxSpeed(77)
                .setContinuousTime((byte) 111)
                .setVerticesCount(areaList.size())
                .setVertices(areaList)
                .setMaxSpeedAtNight(666)
                .setAreaNameLength(areaName.getBytes(JtProtocolConstant.JT_808_STRING_ENCODING).length)
                .setAreaName(areaName);

        final String hexString = this.encode(msg, builder ->
                builder.version(Jt808ProtocolVersion.VERSION_2019)
                        .terminalId("00000000013912344329")
                        .msgId(BuiltinJt808MsgType.SERVER_SET_POLYGON_AREA)
        );

        assertEquals("7E8604403101000000000139123443290000000000640002004D6F"
                + "000201DC9F5A073DA55C01DC0C70073EACB8029A0012C7F8D3F2C3FBB3C6616263646566673132331E7E", hexString);
    }
}
