package io.github.hylexus.jt.jt808.issues.issue91;

import io.github.hylexus.jt.jt808.support.codec.Jt808ByteWriter;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.serializer.extension.AbstractExtendedJt808FieldSerializer;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.netty.buffer.ByteBuf;

import java.util.List;

/**
 * 这个例子全是使用自定义编码器实现的
 * <p>
 * 后续会完善编解码逻辑 支持更复杂的数据类型
 */
public class CustomJt808FieldSerializerIssue91 extends AbstractExtendedJt808FieldSerializer {

    @Override
    public void serialize(Object object, MsgDataType msgDataType, ByteBuf byteBuf, Context context) throws Jt808FieldSerializerException {
        // 8300默认就是字符串，所以这里直接使用默认的编码器
        if (object instanceof String) {
            super.serialize(object, msgDataType, byteBuf, context);
            return;
        }

        // 这里假定你传了个 List
        // 看情况自己修改
        if (!(object instanceof List)) {
            throw new Jt808FieldSerializerException("Unsupported data type: " + object);
        }

        for (final Object value : ((List<?>) object)) {
            if (value == null) {
                continue;
            }
            if (value instanceof Msg80300Issue91.Item0x30) {
                this.encode0x30((Msg80300Issue91.Item0x30) value, byteBuf);
            } else if (value instanceof Msg80300Issue91.Item0x31) {
                this.encode0x31((Msg80300Issue91.Item0x31) value, byteBuf);
            } else {
                throw new Jt808FieldSerializerException("Unsupported data type: " + value);
            }
        }
    }

    private void encode0x31(Msg80300Issue91.Item0x31 value, ByteBuf byteBuf) {
        byteBuf.writeByte(value.getCommand());
        byteBuf.writeShort(value.getLength());
        byteBuf.writeByte(value.getRgbType());
        byteBuf.writeMedium(value.getNumberOfCycles());
        // 20ms 一个单元
        byteBuf.writeShort((int) (value.getDuration().toMillis() / 20));
    }

    private void encode0x30(Msg80300Issue91.Item0x30 item0x30, ByteBuf byteBuf) {
        final Jt808ByteWriter writer = Jt808ByteWriter.of(byteBuf);
        writer.writeByte(item0x30.getCommand());
        writer.writeWord(item0x30.getLength());

        final Msg80300Issue91.Item0x31Data item0x31Data = item0x30.getData();
        writer.writeWord(item0x31Data.getTotalTime());

        for (final Msg80300Issue91.Item0x31DataItem item0x31DataItem : item0x31Data.getDataItemList()) {
            writer.writeByte(item0x31DataItem.getRgbType());
            writer.writeWord(item0x31DataItem.getPwmCount());

            for (final Msg80300Issue91.PulseWidthModulation pwm : item0x31DataItem.getPwmList()) {
                // 20ms 为一个单位
                writer.writeWord((int) (pwm.getTime().toMillis() / 20));
                writer.writeByte(pwm.getPercent());
            }
        }
    }

}
