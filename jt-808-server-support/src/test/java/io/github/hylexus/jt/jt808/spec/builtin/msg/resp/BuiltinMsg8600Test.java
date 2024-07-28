package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.JtProtocolConstant;
import io.github.hylexus.jt.jt808.spec.builtin.msg.BaseReqRespMsgTest;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.utils.BitOperator;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.github.hylexus.oaks.utils.Numbers;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuiltinMsg8600Test extends BaseReqRespMsgTest {

    @Test
    void testBuild0x8600V2011() {
        List<BuiltinMsg8600V2011Alias.Area> areaList = Jdk8Adapter.listOf(
                new BuiltinMsg8600V2011Alias.Area()
                        .setAreaId(100)
                        .setAreaAttr(Numbers.setBitAt(0, 1))
                        .setLat(31000562)
                        .setLng(121451372)
                        .setRadius(2345)
                        .setStartTime(LocalDateTime.of(2023, 4, 17, 22, 16, 33))
                        .setEndTime("230417221655")
                        .setMaxSpeed(77)
                        .setContinuousTime((byte) 111),
                new BuiltinMsg8600V2011Alias.Area()
                        .setAreaId(101)
                        // 0b111
                        .setAreaAttr(BitOperator.mutable(0L).set(0).set(1).set(2).intValue())
                        .setLat(31000562)
                        .setLng(121451372)
                        .setRadius(2345)
                        .setStartTime(LocalDateTime.of(2023, 4, 17, 22, 16, 33))
                        .setEndTime("230417231655")
                        .setMaxSpeed(88)
                        .setContinuousTime((byte) 222)
        );
        final BuiltinMsg8600V2011Alias msg = new BuiltinMsg8600V2011Alias()
                .setType((byte) 1)
                .setTotalAreaCount((byte) areaList.size())
                .setAreas(areaList);

        final String hexString = this.encode(msg, builder ->
                builder.version(Jt808ProtocolVersion.VERSION_2013)
                        .terminalId("013912344323")
                        .msgId(BuiltinJt808MsgType.SERVER_SET_CIRCLE_AREA)
        );
        assertEquals("7E860000380139123443230000010200000064000201D907F2073D336C00000929004D6"
                + "F00000065000701D907F2073D336C000009292304172216332304172316550058DE047E", hexString);
    }

    @Test
    void testBuild0x8600V2019() {
        List<BuiltinMsg8600V2019Alias.Area> areaList = Jdk8Adapter.listOf(
                new BuiltinMsg8600V2019Alias.Area()
                        .setAreaId(100)
                        .setAreaAttr(Numbers.setBitAt(0, 1))
                        .setLat(31000562)
                        .setLng(121451372)
                        .setRadius(2345)
                        .setStartTime(LocalDateTime.of(2023, 4, 17, 22, 16, 33))
                        .setEndTime("230417221655")
                        .setMaxSpeed(77)
                        .setContinuousTime((byte) 111)
                        .setAreaNameLength("区域α".getBytes(JtProtocolConstant.JT_808_STRING_ENCODING).length)
                        .setAreaName("区域α")
                        .setMaxSpeedAtNight(33),
                new BuiltinMsg8600V2019Alias.Area()
                        .setAreaId(101)
                        .setAreaAttr(0b111)
                        .setLat(31000562)
                        .setLng(121451372)
                        .setRadius(2345)
                        .setStartTime(LocalDateTime.of(2023, 4, 17, 22, 16, 33))
                        .setEndTime("230417231655")
                        .setMaxSpeed(88)
                        .setContinuousTime((byte) 222)
                        .setAreaNameLength("区域β".getBytes(JtProtocolConstant.JT_808_STRING_ENCODING).length)
                        .setAreaName("区域β")
                        .setMaxSpeedAtNight(33)
        );
        final BuiltinMsg8600V2019Alias msg = new BuiltinMsg8600V2019Alias()
                .setType((byte) 1)
                .setTotalAreaCount((byte) areaList.size())
                .setAreas(areaList);

        final String hexString = this.encode(msg, builder ->
                builder.version(Jt808ProtocolVersion.VERSION_2019)
                        .terminalId("00000000013912344329")
                        .msgId(BuiltinJt808MsgType.SERVER_SET_CIRCLE_AREA)
        );
        assertEquals("7E8600404C01000000000139123443290000010200000064000201D907F2073D336C000"
                + "00929004D6F00210006C7F8D3F2A6C100000065000701D907F2073D336C000009292304172216332304172"
                + "316550058DE00210006C7F8D3F2A6C2387E", hexString);
    }
}
