package io.github.hylexus.jt.jt1078.support.extension.audio.impl;

import io.github.hylexus.jt.jt1078.support.extension.audio.Jt1078AudioFormat;

/**
 * @author hylexus
 */
public enum DefaultJt1078AudioFormat implements Jt1078AudioFormat {

    PCM(1, "PCM"),
    ADPCM_IMA(-1, "ADPCM/IMA"),
    ADPCM_MS(-2, "ADPCM/MS"),
    ;
    private final int value;
    private final String desc;

    DefaultJt1078AudioFormat(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public int value() {
        return this.value;
    }

    @Override
    public String desc() {
        return this.desc;
    }
}
