package io.github.hylexus.jt.jt1078.support.codec.impl;

import io.github.hylexus.jt.common.Jt808ByteReader;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078MsgDecoder;
import io.github.hylexus.oaks.utils.LongBitOps;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public class DefaultJt1078MsgDecoder implements Jt1078MsgDecoder {

    @Override
    public Jt1078Request decode(ByteBuf input) {
        final Jt1078Request.Jt1078RequestBuilder builder = Jt1078Request.newBuilder()
                .rawByteBuf(input.retainedSlice());

        final Jt808ByteReader reader = Jt808ByteReader.of(input);
        reader.readUnsignedByte(builder::offset4);
        reader.readUnsignedByte(builder::offset5);
        reader.readUnsignedWord(builder::sequenceNumber);
        reader.readBcd(6, builder::sim);
        reader.readUnsignedByte(builder::channelNumber);

        final short offset15 = reader.readUnsignedByte();
        builder.offset15(offset15);

        final byte dataType = Jt1078Request.dataType(offset15);
        final boolean hasTimestampField = Jt1078Request.hasTimestampField(dataType);
        final boolean hasFrameIntervalFields = Jt1078Request.hasFrameIntervalFields(dataType);

        if (hasTimestampField) {
            reader.readBytes(8, offset16 -> {
                final long time = LongBitOps.longFrom8Bytes(offset16);
                builder.timestamp(time);
            });
        }

        if (hasFrameIntervalFields) {
            reader.readUnsignedWord(builder::lastIFrameInterval);
            reader.readUnsignedWord(builder::lastFrameInterval);
        }

        final int msgLengthFieldIndex = Jt1078Request.msgLengthFieldIndex(hasTimestampField, hasFrameIntervalFields);
        final int bodyLength = reader.readUnsignedWord();
        final int readerIndex = input.readerIndex();
        // -4: 0x30316364 --> 4bytes
        // -2: msgBodyLength --> WORD --> 2bytes
        assert readerIndex == msgLengthFieldIndex + 2 - 4;
        final ByteBuf body = input.retainedSlice(readerIndex, bodyLength);
        builder.body(body);
        return builder.build();
    }

}
