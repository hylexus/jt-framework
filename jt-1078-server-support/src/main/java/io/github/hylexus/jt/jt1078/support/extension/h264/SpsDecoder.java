package io.github.hylexus.jt.jt1078.support.extension.h264;

import io.netty.buffer.ByteBuf;

public interface SpsDecoder {

    Sps decodeSps(ByteBuf input);

}
