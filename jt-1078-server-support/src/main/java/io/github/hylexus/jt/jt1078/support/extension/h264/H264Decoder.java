package io.github.hylexus.jt.jt1078.support.extension.h264;

import io.github.hylexus.jt.jt1078.spec.Jt1078Request;

import java.util.List;

public interface H264Decoder {

    List<H264Nalu> decode(Jt1078Request request);

}
