package io.github.hylexus.jt.dashboard.server.model.constants;

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
