package io.github.hylexus.jt.data.converter.req.entity;

import io.github.hylexus.oaks.utils.IntBitOps;

public class LngLatReqMsgFieldConverter implements ReqMsgFieldConverter<Double> {

    @Override
    public Double convert(byte[] bytes, byte[] subSeq) {
        return IntBitOps.intFromBytes(subSeq, 0, subSeq.length) * 1.0 / 100_0000;
    }

}