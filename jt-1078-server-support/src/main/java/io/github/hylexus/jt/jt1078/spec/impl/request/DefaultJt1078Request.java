package io.github.hylexus.jt.jt1078.spec.impl.request;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.netty.buffer.ByteBuf;

import java.util.Optional;

/**
 * @author hylexus
 */
@BuiltinComponent
public class DefaultJt1078Request implements Jt1078Request, Jt1078Request.Jt1078RequestBuilder {

    private ByteBuf rawByteBuf;
    private ByteBuf body;
    private short offset4;
    private short offset5;
    private int sequenceNumber;
    private String sim;
    private short channelNumber;
    private short offset15;

    private Long timestamp;
    private Integer lastIFrameInterval;
    private Integer lastFrameInterval;

    private int msgBodyLength;

    protected DefaultJt1078Request(
            ByteBuf rawByteBuf, ByteBuf body,
            short offset4, short offset5, int sequenceNumber, String sim,
            short channelNumber, short offset15,
            Long timestamp, Integer lastIFrameInterval, Integer lastFrameInterval,
            int msgBodyLength) {

        this.rawByteBuf = rawByteBuf;
        this.body = body;
        this.offset4 = offset4;
        this.offset5 = offset5;
        this.sequenceNumber = sequenceNumber;
        this.sim = sim;
        this.channelNumber = channelNumber;
        this.offset15 = offset15;
        this.timestamp = timestamp;
        this.lastIFrameInterval = lastIFrameInterval;
        this.lastFrameInterval = lastFrameInterval;
        this.msgBodyLength = msgBodyLength;
    }

    public DefaultJt1078Request() {
    }

    public DefaultJt1078Request(Jt1078Request another) {
        this.rawByteBuf(another.rawByteBuf())
                .body(another.body())
                .offset4(another.offset4())
                .offset5(another.offset5())
                .sequenceNumber(another.sequenceNumber())
                .sim(another.sim())
                .channelNumber(another.channelNumber())
                .offset15(another.offset15())
                .timestamp(another.timestamp().orElse(null))
                .lastIFrameInterval(another.lastIFrameInterval().orElse(null))
                .lastFrameInterval(another.lastFrameInterval().orElse(null));
    }

    @Override
    public ByteBuf rawByteBuf() {
        return this.rawByteBuf;
    }

    @Override
    public Jt1078RequestBuilder rawByteBuf(ByteBuf rawByteBuf, boolean autoRelease) {
        final ByteBuf oldBuf = this.rawByteBuf;
        try {
            this.rawByteBuf = rawByteBuf;
            return this;
        } finally {
            if (autoRelease) {
                JtCommonUtils.release(oldBuf);
            }
        }
    }

    @Override
    public ByteBuf body() {
        return this.body;
    }

    @Override
    public Jt1078RequestBuilder body(ByteBuf body, boolean autoRelease) {
        final ByteBuf oldBuf = this.body;
        try {
            this.body = body;
            if (body != null) {
                this.msgBodyLength = body.readableBytes();
            }
            return this;
        } finally {
            if (autoRelease) {
                JtCommonUtils.release(oldBuf);
            }
        }
    }

    @Override
    public short offset4() {
        return this.offset4;
    }

    @Override
    public Jt1078RequestBuilder offset4(short offset4) {
        this.offset4 = offset4;
        return this;
    }

    @Override
    public short offset5() {
        return this.offset5;
    }

    @Override
    public Jt1078RequestBuilder offset5(short offset5) {
        this.offset5 = offset5;
        return this;
    }

    @Override
    public int sequenceNumber() {
        return this.sequenceNumber;
    }

    @Override
    public Jt1078RequestBuilder sequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
    }

    @Override
    public String sim() {
        return this.sim;
    }

    @Override
    public Jt1078RequestBuilder sim(String sim) {
        this.sim = sim;
        return this;
    }

    @Override
    public short channelNumber() {
        return this.channelNumber;
    }

    @Override
    public Jt1078RequestBuilder channelNumber(short channelNumber) {
        this.channelNumber = channelNumber;
        return this;
    }

    @Override
    public short offset15() {
        return this.offset15;
    }

    @Override
    public Jt1078RequestBuilder offset15(short offset15) {
        this.offset15 = offset15;
        return this;
    }

    @Override
    public Optional<Long> timestamp() {
        return Optional.ofNullable(this.timestamp);
    }

    @Override
    public Jt1078RequestBuilder timestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public Optional<Integer> lastIFrameInterval() {
        return Optional.ofNullable(this.lastIFrameInterval);
    }


    @Override
    public Jt1078RequestBuilder lastIFrameInterval(Integer lastIFrameInterval) {
        this.lastIFrameInterval = lastIFrameInterval;
        return this;
    }

    @Override
    public Optional<Integer> lastFrameInterval() {
        return Optional.ofNullable(this.lastFrameInterval);
    }

    @Override
    public Jt1078RequestBuilder lastFrameInterval(Integer lastFrameInterval) {
        this.lastFrameInterval = lastFrameInterval;
        return this;
    }

    @Override
    public int msgBodyLength() {
        return this.msgBodyLength;
    }

    @Override
    public Jt1078Request build() {
        return new DefaultJt1078Request(
                this.rawByteBuf, this.body,
                this.offset4, this.offset5,
                this.sequenceNumber, this.sim, this.channelNumber, this.offset15,
                this.timestamp, this.lastIFrameInterval, this.lastFrameInterval,
                this.msgBodyLength
        );
    }

    @Override
    public String toString() {
        return "DefaultJt1078Request{"
               + "offset4=" + offset4
               + ", V=" + v()
               + ", P=" + p()
               + ", X=" + x()
               + ", CC=" + cc()
               + ", offset5=" + offset5
               + ", M=" + m()
               + ", PT=" + pt()
               + ", sequenceNumber=" + sequenceNumber
               + ", sim='" + sim + '\''
               + ", channelNumber=" + channelNumber
               + ", offset15=" + offset15
               + ", dataType=" + dataType()
               + ", subPackageIdentifier=" + subPackageIdentifier()
               + ", timestamp=" + timestamp
               + ", lastIFrameInterval=" + lastIFrameInterval
               + ", lastFrameInterval=" + lastFrameInterval
               + ", msgBodyLength=" + msgBodyLength
               + '}';
    }
}
