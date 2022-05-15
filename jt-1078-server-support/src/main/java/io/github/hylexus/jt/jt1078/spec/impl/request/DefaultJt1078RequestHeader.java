package io.github.hylexus.jt.jt1078.spec.impl.request;

import io.github.hylexus.jt.jt1078.spec.Jt1078DataType;
import io.github.hylexus.jt.jt1078.spec.Jt1078PayloadType;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestHeader;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubPackageIdentifier;

import java.util.Optional;

/**
 * @author hylexus
 */
public class DefaultJt1078RequestHeader implements Jt1078RequestHeader, Jt1078RequestHeader.Jt1078RequestHeaderBuilder {

    private short offset4;
    private short offset5;
    private Jt1078PayloadType payloadType;
    private int offset6;
    private String offset8;
    private short offset14;
    private short offset15;
    private Jt1078DataType dataType;
    private Jt1078SubPackageIdentifier subPackageIdentifier;
    private Long offset16;
    private Integer offset24;
    private Integer offset26;
    private int offset28;

    public DefaultJt1078RequestHeader() {
    }

    public DefaultJt1078RequestHeader(
            short offset4, short offset5,
            int offset6, String offset8,
            short offset14, short offset15,
            Long offset16, Integer offset24,
            Integer offset26, int offset28) {

        this.offset4 = offset4;
        this.offset5(offset5);
        this.offset6 = offset6;
        this.offset8 = offset8;
        this.offset14 = offset14;
        this.offset15(offset15);
        this.offset16 = offset16;
        this.offset24 = offset24;
        this.offset26 = offset26;
        this.offset28 = offset28;
    }

    public DefaultJt1078RequestHeader(Jt1078RequestHeader another) {
        this.offset4(another.offset4())
                .offset5(another.offset5())
                .offset6(another.offset6())
                .offset8(another.offset8())
                .offset16(another.offset16().orElse(null))
                .offset24(another.offset24().orElse(null))
                .offset26(another.offset26().orElse(null))
                .offset28(another.offset28());
    }

    @Override
    public short offset4() {
        return this.offset4;
    }

    @Override
    public Jt1078RequestHeaderBuilder offset4(short value) {
        this.offset4 = value;
        return this;
    }

    @Override
    public short offset5() {
        return this.offset5;
    }

    @Override
    public Jt1078RequestHeaderBuilder offset5(short value) {
        this.offset5 = value;
        this.payloadType = Jt1078PayloadType.createOrDefault(this.pt());
        return this;
    }

    @Override
    public Jt1078PayloadType payloadType() {
        return this.payloadType;
    }

    @Override
    public int offset6() {
        return this.offset6;
    }

    @Override
    public Jt1078RequestHeaderBuilder offset6(int value) {
        this.offset6 = value;
        return this;
    }

    @Override
    public String offset8() {
        return this.offset8;
    }

    @Override
    public Jt1078RequestHeaderBuilder offset8(String value) {
        this.offset8 = value;
        return this;
    }

    @Override
    public short offset14() {
        return this.offset14;
    }

    @Override
    public Jt1078RequestHeaderBuilder offset14(short value) {
        this.offset14 = value;
        return this;
    }


    @Override
    public short offset15() {
        return this.offset15;
    }

    @Override
    public Jt1078RequestHeaderBuilder offset15(short value) {
        this.offset15 = value;
        this.dataType = Jt1078DataType.createOrDefault(this.dataTypeValue());
        this.subPackageIdentifier = Jt1078SubPackageIdentifier.defaultOrCreate(this.subPackageIdentifierValue());
        return this;
    }

    @Override
    public Jt1078DataType dataType() {
        return dataType;
    }

    @Override
    public Jt1078SubPackageIdentifier subPackageIdentifier() {
        return subPackageIdentifier;
    }

    @Override
    public Optional<Long> offset16() {
        return Optional.ofNullable(this.offset16);
    }

    @Override
    public Jt1078RequestHeaderBuilder offset16(Long value) {
        this.offset16 = value;
        return this;
    }

    @Override
    public Optional<Integer> offset24() {
        return Optional.ofNullable(this.offset24);
    }

    @Override
    public Jt1078RequestHeaderBuilder offset24(Integer value) {
        this.offset24 = value;
        return this;
    }

    @Override
    public Optional<Integer> offset26() {
        return Optional.ofNullable(this.offset26);
    }

    @Override
    public Jt1078RequestHeaderBuilder offset26(Integer value) {
        this.offset26 = value;
        return this;
    }

    @Override
    public int offset28() {
        return this.offset28;
    }

    @Override
    public Jt1078RequestHeaderBuilder offset28(int value) {
        this.offset28 = value;
        return this;
    }

    @Override
    public Jt1078RequestHeader build() {
        return new DefaultJt1078RequestHeader(
                this.offset4, this.offset5, this.offset6, this.offset8, this.offset14,
                this.offset15, this.offset16, this.offset24, this.offset26, this.offset28
        );
    }

    @Override
    public String toString() {
        return "DefaultJt1078RequestHeader{"
                + "offset4=" + offset4
                + ", P=" + p()
                + ", X=" + x()
                + ", CC=" + cc()
                + ", offset5=" + offset5
                + ", M=" + m()
                + ", PT=" + payloadType()
                + ", offset6(sequenceNumber)=" + offset6
                + ", offset8(sim)='" + sim() + '\''
                + ", offset14(channelNumber)=" + channelNumber()
                + ", offset15=" + offset15
                + ", dataType=" + dataType
                + ", subPackageIdentifier=" + subPackageIdentifier
                + ", offset16(timestamp)=" + offset16
                + ", offset24(lastIFrameInterval)=" + offset24
                + ", offset26(lastFrameInterval)=" + offset26
                + ", offset28(msgBodyLength)=" + offset28
                + '}';
    }
}
