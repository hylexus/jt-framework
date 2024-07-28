package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.JtProtocolConstant;
import io.github.hylexus.jt.jt808.spec.builtin.msg.BaseReqRespMsgTest;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.github.hylexus.oaks.utils.Numbers;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuiltinMsg8602Test extends BaseReqRespMsgTest {

    @Test
    void testBuild0x8602V2011() throws ParseException {
        List<BuiltinMsg8602V2011Alias.Area> areaList = Jdk8Adapter.listOf(
                new BuiltinMsg8602V2011Alias.Area()
                        .setAreaId(100)
                        .setAreaAttr(Numbers.setBitAt(0, 1))
                        .setLeftTopLng(121480540)
                        .setLeftTopLat(31235930)
                        .setRightBottomLng(121547960)
                        .setRightBottomLat(31198320)
                        .setStartTime(LocalDateTime.of(2023, 4, 17, 22, 16, 33))
                        .setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2023-04-18 22:22:12"))
                        .setMaxSpeed(77)
                        .setContinuousTime((byte) 111),
                new BuiltinMsg8602V2011Alias.Area()
                        .setAreaId(101)
                        .setAreaAttr(0b111)
                        .setLeftTopLng(121480540)
                        .setLeftTopLat(31235930)
                        .setRightBottomLng(121547960)
                        .setRightBottomLat(31198320)
                        .setStartTime(LocalDateTime.of(2023, 4, 17, 22, 16, 33))
                        .setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2023-04-18 22:22:12"))
                        .setMaxSpeed(88)
                        .setContinuousTime((byte) 222)
        );
        final BuiltinMsg8602V2011Alias msg = new BuiltinMsg8602V2011Alias()
                .setType((byte) 1)
                .setTotalAreaCount((byte) areaList.size())
                .setAreas(areaList);


        String hexString = this.encode(msg, builder ->
                builder.version(Jt808ProtocolVersion.VERSION_2013)
                        .terminalId("013912344323")
                        .msgId(BuiltinJt808MsgType.SERVER_SET_RECTANGLE_AREA)
        );
        assertEquals("7E860200400139123443230000010200000064000201DC9F5A073DA55C01DC0C70073E"
                + "ACB8004D6F00000065000701DC9F5A073DA55C01DC0C70073EACB82304172216332304182222120058DE037E", hexString);
    }

    @Test
    void testBuild0x8602V2019() throws ParseException {
        List<BuiltinMsg8602V2019Alias.Area> areaList = Jdk8Adapter.listOf(
                new BuiltinMsg8602V2019Alias.Area()
                        .setAreaId(100)
                        .setAreaAttr(0b1)
                        .setLeftTopLng(121480540)
                        .setLeftTopLat(31235930)
                        .setRightBottomLng(121547960)
                        .setRightBottomLat(31198320)
                        .setStartTime(LocalDateTime.of(2023, 4, 17, 22, 16, 33))
                        .setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2023-04-18 22:22:12"))
                        .setMaxSpeed(77)
                        .setContinuousTime((byte) 111)
                        .setMaxSpeedAtNight(6767)
                        .setAreaNameLength("区域A".getBytes(JtProtocolConstant.JT_808_STRING_ENCODING).length)
                        .setAreaName("区域A"),
                new BuiltinMsg8602V2019Alias.Area()
                        .setAreaId(101)
                        .setAreaAttr(0b111)
                        .setLeftTopLng(121480540)
                        .setLeftTopLat(31235930)
                        .setRightBottomLng(121547960)
                        .setRightBottomLat(31198320)
                        .setStartTime(LocalDateTime.of(2023, 4, 17, 22, 16, 33))
                        .setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2023-04-18 22:22:12"))
                        .setMaxSpeed(88)
                        .setContinuousTime((byte) 99)
                        .setMaxSpeedAtNight(6768)
                        .setAreaNameLength("区域B".getBytes(JtProtocolConstant.JT_808_STRING_ENCODING).length)
                        .setAreaName("区域B")
        );
        final BuiltinMsg8602V2019Alias msg = new BuiltinMsg8602V2019Alias()
                .setType((byte) 1)
                .setTotalAreaCount((byte) areaList.size())
                .setAreas(areaList);


        final String hexString = this.encode(msg, builder ->
                builder.version(Jt808ProtocolVersion.VERSION_2019)
                        .terminalId("00000000013912344329")
                        .msgId(BuiltinJt808MsgType.SERVER_SET_RECTANGLE_AREA)
        );
        assertEquals("7E8602405901000000000139123443290000010200000064000101DC9F"
                + "5A073DA55C01DC0C70073EACB82304172216332304182222120005C7F8D3F24100000065000"
                + "701DC9F5A073DA55C01DC0C70073EACB82304172216332304182222120058631A700005C7F8D3F242BE7E", hexString);
    }
}
