package io.github.hylexus.jt.jt1078.support.codec.impl;

import io.github.hylexus.jt.common.Jt808ByteReader;
import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestHeader;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078MsgDecoder;
import io.github.hylexus.oaks.utils.LongBitOps;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public class DefaultJt1078MsgDecoder implements Jt1078MsgDecoder {

    @Override
    public Jt1078Request decode(ByteBuf input) {

        final ByteBuf rawByteBuf = input.retainedSlice();
        final ByteBuf readable = input.retainedSlice();
        try {
            final Jt1078RequestHeader.Jt1078RequestHeaderBuilder headerBuilder = Jt1078RequestHeader.newBuilder();
            final Jt808ByteReader reader = Jt808ByteReader.of(readable);

            reader.readUnsignedByte(headerBuilder::offset4);
            reader.readUnsignedByte(headerBuilder::offset5);
            reader.readUnsignedWord(headerBuilder::offset6);
            reader.readBcd(6, headerBuilder::offset8);
            reader.readUnsignedByte(headerBuilder::offset14);

            final short offset15 = reader.readUnsignedByte();
            headerBuilder.offset15(offset15);

            final byte dataType = Jt1078RequestHeader.dataTypeValue(offset15);
            final boolean hasTimestampField = Jt1078RequestHeader.hasTimestampField(dataType);
            final boolean hasFrameIntervalFields = Jt1078RequestHeader.hasFrameIntervalFields(dataType);

            if (hasTimestampField) {
                reader.readBytes(8, offset16 -> {
                    final long time = LongBitOps.longFrom8Bytes(offset16);
                    headerBuilder.offset16(time);
                });
            }

            if (hasFrameIntervalFields) {
                reader.readUnsignedWord(headerBuilder::offset24);
                reader.readUnsignedWord(headerBuilder::offset26);
            }

            final int msgLengthFieldIndex = Jt1078RequestHeader.msgLengthFieldIndex(hasTimestampField, hasFrameIntervalFields);
            final int bodyLength = reader.readUnsignedWord();
            headerBuilder.offset28(bodyLength);
            final int readerIndex = readable.readerIndex();
            // -4: 0x30316364 --> 4bytes
            // +2: msgBodyLength --> WORD --> 2bytes
            assert readerIndex == msgLengthFieldIndex + 2 - 4;
            final ByteBuf body = readable.slice(readerIndex, bodyLength);

            final Jt1078RequestHeader header = headerBuilder.build();

            return Jt1078Request.newBuilder()
                    .header(header)
                    .body(body)
                    .rawByteBuf(rawByteBuf)
                    .build();
        } catch (Throwable e) {
            JtCommonUtils.release(readable, rawByteBuf);
            throw new RuntimeException(e);
        }
    }

}
