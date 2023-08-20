package io.github.hylexus.jt.demos.dashboard.boot3.model.constants;

import lombok.Getter;

@Getter
public enum StreamType {
    MAIN_STREAM(0),
    SUB_STREAM(1);

    private final int value;

    StreamType(int value) {
        this.value = value;
    }

}
