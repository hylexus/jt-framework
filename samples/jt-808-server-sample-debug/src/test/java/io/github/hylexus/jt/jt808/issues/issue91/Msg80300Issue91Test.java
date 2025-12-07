package io.github.hylexus.jt.jt808.issues.issue91;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808MsgBuilder;
import io.github.hylexus.jt.jt808.support.codec.Jt808ByteReader;
import io.github.hylexus.jt.utils.FormatUtils;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Msg80300Issue91Test {
    @Test
    void test() {
        final Msg80300Issue91 body = createBody();
        final ByteBuf byteBuf = Jt808MsgBuilder.newEntityBuilder(step -> 0)
                .version(Jt808ProtocolVersion.VERSION_2013)
                .terminalId("025470701308")
                .body(body)
                .msgId(0x8300)
                .build();
        System.out.println(FormatUtils.toHexString(byteBuf));
    }

    private Msg80300Issue91 createBody() {
        final Msg80300Issue91 body = new Msg80300Issue91();
        body.setFlag((byte) 0x01);
        // body.setData(Jdk8Adapter.listOf(createItem0x30()));
        body.setData(Jdk8Adapter.listOf(createItem0x30(), createItem0x31()));
        return body;
    }

    private static Msg80300Issue91.Item0x31 createItem0x31() {
        return new Msg80300Issue91.Item0x31()
                .setRgbType(1)
                .setNumberOfCycles(18)
                .setDuration(Duration.ofSeconds(1));
    }

    private static Msg80300Issue91.Item0x30 createItem0x30() {
        final Msg80300Issue91.Item0x31Data item0x31Data = createItem0x31Data();
        final Msg80300Issue91.Item0x30 item0x30 = new Msg80300Issue91.Item0x30()
                .setCommand(0x30)
                // 先给个 -1
                // 等数据都设置完成之后 再计算总长度
                .setLength(-1)
                .setData(item0x31Data);

        // 等数据都设置完成之后 再计算总长度
        final int totalLength = calculateTotalBytes(item0x30);
        item0x30.setLength(totalLength);
        return item0x30;
    }

    private static int calculateTotalBytes(Msg80300Issue91.Item0x30 item0x30) {

        int totalLength = 0;
        for (final Msg80300Issue91.Item0x31DataItem item0x31DataItem : item0x30.getData().getDataItemList()) {
            // +1 byte: rgbType
            // +2 bytes: 段落数
            // + N 个 [时长+百分比] === N 个 [时长(2字节) + 百分比(1字节)] === N * 3
            final int l = 1 + 2 + item0x31DataItem.getPwmList().size() * 3;
            totalLength += l;
        }
        // +2 bytes: 总时间的字节数
        totalLength += 2;
        return totalLength;
    }

    private static Msg80300Issue91.Item0x31Data createItem0x31Data() {

        final List<Msg80300Issue91.Item0x31DataItem> dataItemList = createDataItemList();
        final Msg80300Issue91.Item0x31Data result = new Msg80300Issue91.Item0x31Data()
                // 先给个 -1
                // 等最后再赋值
                .setTotalTime(-1)
                .setDataItemList(dataItemList);

        // 总时间: 2字节(0表示一直循环)，单位:秒
        // todo 这个时间的含义请和设备方确认: 到底指的所有 PWM 的时间总和？ 还是说其他含义，这里先当做总和计算了
        final int totalTime = dataItemList.stream()
                .flatMap(it -> it.getPwmList().stream())
                .mapToInt(it -> (int) it.getTime().toMillis())
                .sum();
        result.setTotalTime(totalTime / 1000);
        return result;
    }

    private static List<Msg80300Issue91.Item0x31DataItem> createDataItemList() {
        final Msg80300Issue91.Item0x31DataItem item0x31DataItem1 = new Msg80300Issue91.Item0x31DataItem()
                // 红灯
                .setRgbType(1)
                // 先给个 -1
                // 等 PWM list 设置完成之后再赋值 pwmCount
                .setPwmCount(-1)
                .setPwmList(Jdk8Adapter.listOf(
                        // 红灯 %0 的亮度持续 3s
                        new Msg80300Issue91.PulseWidthModulation(Duration.ofSeconds(3), 33),
                        // 红灯 %50 的亮度持续 2s
                        new Msg80300Issue91.PulseWidthModulation(Duration.ofSeconds(2), 66),
                        // 红灯 %100 的亮度持续 1s
                        new Msg80300Issue91.PulseWidthModulation(Duration.ofSeconds(1), 100)
                ));
        // 等 PWM list 设置完成之后再赋值 pwmCount
        item0x31DataItem1.setPwmCount(item0x31DataItem1.getPwmList().size());

        final Msg80300Issue91.Item0x31DataItem item0x31DataItem2 = new Msg80300Issue91.Item0x31DataItem()
                // 绿灯
                .setRgbType(2)
                // 先给个 -1
                // 等 PWM list 设置完成之后再赋值 pwmCount
                .setPwmCount(-1)
                .setPwmList(Jdk8Adapter.listOf(
                        // 绿灯 %0 的亮度持续 1s
                        new Msg80300Issue91.PulseWidthModulation(Duration.ofSeconds(1), 0),
                        // 绿灯 %50 的亮度持续 2s
                        new Msg80300Issue91.PulseWidthModulation(Duration.ofSeconds(2), 50),
                        // 绿灯 %100 的亮度持续 3s
                        new Msg80300Issue91.PulseWidthModulation(Duration.ofSeconds(3), 100)
                ));
        // 等 PWM list 设置完成之后再赋值 pwmCount
        item0x31DataItem2.setPwmCount(item0x31DataItem2.getPwmList().size());

        final Msg80300Issue91.Item0x31DataItem item0x31DataItem3 = new Msg80300Issue91.Item0x31DataItem()
                // 蓝灯
                .setRgbType(3)
                // 先给个 -1
                // 等 PWM list 设置完成之后再赋值 pwmCount
                .setPwmCount(-1)
                .setPwmList(Jdk8Adapter.listOf(
                        // 蓝灯 %0 的亮度持续 1s
                        new Msg80300Issue91.PulseWidthModulation(Duration.ofSeconds(1), 0),
                        // 蓝灯 %50 的亮度持续 2s
                        new Msg80300Issue91.PulseWidthModulation(Duration.ofSeconds(2), 50),
                        // 蓝灯 %100 的亮度持续 3s
                        new Msg80300Issue91.PulseWidthModulation(Duration.ofSeconds(3), 100)
                ));
        // 等 PWM list 设置完成之后再赋值 pwmCount
        item0x31DataItem3.setPwmCount(item0x31DataItem3.getPwmList().size());
        return Jdk8Adapter.listOf(item0x31DataItem1, item0x31DataItem2, item0x31DataItem3);
    }

    @Test
    void test2() {
        final String hex = "3000260012010003009621006442003264020003003200006432009664030003003200006432009664";
        final ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer().writeBytes(HexStringUtils.hexString2Bytes(hex));

        final Msg80300Issue91.Item0x31Data item0x31Data = new Msg80300Issue91.Item0x31Data();
        final Msg80300Issue91.Item0x30 item0x30 = new Msg80300Issue91.Item0x30()
                .setData(item0x31Data);

        final Jt808ByteReader reader = Jt808ByteReader.of(byteBuf);
        reader.readByte(item0x30::setCommand);
        reader.readWord(item0x30::setLength);

        reader.readWord(item0x31Data::setTotalTime);
        final ArrayList<Msg80300Issue91.Item0x31DataItem> dataItemList = new ArrayList<>();
        item0x31Data.setDataItemList(dataItemList);
        while (reader.readable().isReadable()) {
            final Msg80300Issue91.Item0x31DataItem item = new Msg80300Issue91.Item0x31DataItem();
            dataItemList.add(item);
            reader.readByte(item::setRgbType);
            final int pwmCount = reader.readWord();
            item.setPwmCount(pwmCount);
            item.setPwmList(new ArrayList<>());
            for (int i = 0; i < pwmCount; i++) {
                final Msg80300Issue91.PulseWidthModulation pwm = new Msg80300Issue91.PulseWidthModulation();
                item.getPwmList().add(pwm);
                reader.readWord(time -> {
                    final int millis = time * 20;
                    pwm.setTime(Duration.ofMillis(millis));
                });
                reader.readByte(pwm::setPercent);
            }
        }

        System.out.println(item0x31Data);
    }
}
