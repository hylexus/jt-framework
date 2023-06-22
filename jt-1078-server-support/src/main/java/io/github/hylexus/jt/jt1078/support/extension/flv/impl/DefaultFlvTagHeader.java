package io.github.hylexus.jt.jt1078.support.extension.flv.impl;

import io.github.hylexus.jt.jt1078.support.extension.flv.FlvTagHeader;
import io.github.hylexus.jt.jt1078.support.extension.flv.tag.FlvTagType;

public class DefaultFlvTagHeader implements FlvTagHeader {

    private final FlvTagType type;
    private final int dataSize;
    private final int timestamp;

    public DefaultFlvTagHeader(FlvTagType type, int dataSize, int timestamp) {
        this.dataSize = dataSize;
        this.timestamp = timestamp;
        this.type = type;
    }

    @Override
    public FlvTagType tagTye() {
        return this.type;
    }

    @Override
    public int dataSize() {
        return this.dataSize;
    }

    @Override
    public int timestamp() {
        return this.timestamp;
    }

    @Override
    public byte timestampExtended() {
        return 0;
    }
}