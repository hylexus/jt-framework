package io.github.hylexus.jt.jt1078.support.extension.h264;

import io.github.hylexus.jt.jt1078.spec.Jt1078DataType;
import io.netty.buffer.ByteBuf;

import java.util.Optional;

public interface H264Nalu {
    // 0001 or 001
    default byte[] startCode() {
        return new byte[]{0, 0, 0, 1};
    }

    // nalu header
    H264NaluHeader header();

    // nalu data: rbsp(RawByte Sequence Payload)
    ByteBuf data();

    // extra msg from jt1078
    Jt1078DataType dataType();

    Optional<Integer> lastIFrameInterval();

    Optional<Integer> lastFrameInterval();

    Optional<Long> timestamp();
}
