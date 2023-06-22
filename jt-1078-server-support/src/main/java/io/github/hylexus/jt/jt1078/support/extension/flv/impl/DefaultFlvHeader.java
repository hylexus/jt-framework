package io.github.hylexus.jt.jt1078.support.extension.flv.impl;

import io.github.hylexus.jt.jt1078.support.extension.flv.FlvHeader;

public class DefaultFlvHeader implements FlvHeader {

    private final byte flag;

    public DefaultFlvHeader(boolean hasVideo, boolean hasAudio) {
        this.flag = (byte) ((hasVideo ? 0b1 : 0) | (hasAudio ? 0b0100 : 0));
    }

    @Override
    public byte flag() {
        return this.flag;
    }

}
