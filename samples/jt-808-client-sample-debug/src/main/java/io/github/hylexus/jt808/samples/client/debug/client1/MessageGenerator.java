package io.github.hylexus.jt808.samples.client.debug.client1;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.spec.impl.msg.builder.ByteBufJt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGenerator;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.github.hylexus.jt808.samples.client.debug.client1.entity.ClientLocationUploadMsgV2019;
import io.github.hylexus.jt808.samples.client.debug.client1.entity.ClientRegisterMsgV2013;
import io.github.hylexus.oaks.utils.IntBitOps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.VERSION_2013;
import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.VERSION_2019;

/**
 * @author hylexus
 */
public class MessageGenerator {
    private static final int[] mileages = {100, 0x7d, 0x7e};

    // public static void main(String[] args) {
    //    final MessageGenerator generator = new MessageGenerator();
    //    //final ByteBuf byteBuf = new MessageGenerator()
    //    .randomClientRegisterV2013(new DefaultJt808FlowIdGenerator(), formatTerminalId("666", Jt808ProtocolVersion.VERSION_2013));
    //    //final ByteBuf byteBuf = generator.randomClientRegisterV2019(new DefaultJt808FlowIdGenerator(), formatTerminalId("666", VERSION_2019));
    //    final ByteBuf byteBuf = generator.randomLocationMsgV2013(new DefaultJt808FlowIdGenerator(), formatTerminalId("666", VERSION_2013),33);
    //    //final ByteBuf byteBuf = generator.randomLocationMsgV2019(new DefaultJt808FlowIdGenerator(), formatTerminalId("666", VERSION_2019),33);
    //    System.out.println(HexStringUtils.byteBufToString(byteBuf));
    //}

    public static List<String> generateTerminalIds(int count) {
        final Set<String> set = new HashSet<>();
        while (set.size() < count) {
            set.add(terminalId());
        }
        return new ArrayList<>(set);
    }

    private static String terminalId() {
        StringBuilder builder = new StringBuilder("123456");
        while (builder.length() < 12) {
            builder.insert(0, ThreadLocalRandom.current().nextInt(10));
        }
        return builder.toString();
    }

    public static String formatTerminalId(String input, Jt808ProtocolVersion version) {
        if (version == VERSION_2019) {
            if (input.length() > 20) {
                return input.substring(input.length() - 20);
            } else if (input.length() < 20) {
                StringBuilder sb = new StringBuilder(input);
                while (sb.length() < 20) {
                    sb.insert(0, "0");
                }
                return sb.toString();
            } else {
                return input;
            }
        } else {
            if (input.length() > 12) {
                return input.substring(input.length() - 12);
            } else if (input.length() < 12) {
                StringBuilder sb = new StringBuilder(input);
                while (sb.length() < 12) {
                    sb.insert(0, "0");
                }
                return sb.toString();
            } else {
                return input;
            }
        }
    }

    public ByteBuf randomClientRegisterV2019(Jt808FlowIdGenerator flowIdGenerator, String terminalId) {
        try (ByteBufJt808MsgBuilder builder = Jt808MsgBuilder.newByteBufBuilder(flowIdGenerator, ByteBufAllocator.DEFAULT.buffer(128))) {
            return builder
                    .version(VERSION_2019)
                    .msgId(BuiltinJt808MsgType.CLIENT_REGISTER)
                    .terminalId(terminalId)
                    .body(writer -> writer
                            // 省域ID WORD
                            .writeWord(11)
                            // 市县域ID WORD
                            .writeWord(2)
                            // 制造商ID byte[11]
                            .writeString("id987654321")
                            // 终端型号 byte[30]
                            .writeString("type00123456781234567887654321")
                            // 终端ID byte[30]
                            .writeString("ID0000123456781234567887654321")
                            .writeByte(1)
                            .writeString("甘J-123459")
                    )
                    .build();
        }
    }

    public ByteBuf randomClientRegisterV2013(Jt808FlowIdGenerator flowIdGenerator, String terminalId) {
        final ClientRegisterMsgV2013 entity = new ClientRegisterMsgV2013()
                .setProvinceId(11)
                .setCityId(2)
                .setManufacturerId("id123")
                .setTerminalType("type1234567887654321")
                .setTerminalId("ID12345")
                .setColor((byte) 1)
                .setCarIdentifier("甘J-123453");

        return Jt808MsgBuilder.newEntityBuilder(flowIdGenerator)
                .version(Jt808ProtocolVersion.VERSION_2013)
                .terminalId(terminalId)
                .body(entity)
                .build();
    }

    public ByteBuf randomLocationMsgV2013(Jt808FlowIdGenerator flowIdGenerator, String terminalId, int maxPackageSize) {
        final int mileage = mileages[new Random().nextInt(3)];
        final ClientLocationUploadMsgV2019 entity = new ClientLocationUploadMsgV2019()
                .setHeight(22)
                .setSpeed(80)
                .setDirection(111)
                .setLng(121480540 + ThreadLocalRandom.current().nextInt(100000))
                .setLat(31235930 + ThreadLocalRandom.current().nextInt(100000))
                .setTime("220322001214")
                .setExtraItemList(Jdk8Adapter.listOf(
                        new ClientLocationUploadMsgV2019.ExtraItem().setId(0x01).setContentLength(4).setContent(IntBitOps.intTo4Bytes(mileage))
                ));

        return Jt808MsgBuilder.newEntityBuilder(flowIdGenerator)
                .terminalId(terminalId)
                .msgId(BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD)
                .maxPackageSize(maxPackageSize)
                .version(VERSION_2013)
                .body(entity)
                .build();
    }

    public ByteBuf randomLocationMsgV2019(Jt808FlowIdGenerator flowIdGenerator, String terminalId, int maxPackageSize) {
        final int mileage = mileages[new Random().nextInt(3)];

        try (ByteBufJt808MsgBuilder builder = Jt808MsgBuilder.newByteBufBuilder(flowIdGenerator, ByteBufAllocator.DEFAULT.buffer(256))) {
            return builder
                    .version(VERSION_2019)
                    .msgId(BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD)
                    .terminalId(terminalId)
                    .maxPackageSize(maxPackageSize)
                    .body(writer -> writer
                            .writeDWord(0)
                            .writeDWord(0)
                            .writeDWord(31235930 + ThreadLocalRandom.current().nextInt(100000))
                            .writeDWord(121480540 + ThreadLocalRandom.current().nextInt(100000))
                            // 16 height
                            .writeWord(22)
                            .writeWord(100)
                            .writeWord(11)
                            // 22 time
                            .writeBcd("220322001214")
                            // item1
                            .writeByte(0x01)
                            .writeByte(4)
                            .writeDWord(ThreadLocalRandom.current().nextInt(mileage))
                            //
                            // item2
                            .writeByte(0x02)
                            .writeByte(2)
                            .writeWord(99)
                    )
                    .build();
        }
    }
}
