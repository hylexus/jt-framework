package io.github.hylexus.jt808.samples.annotation.model.vo;

public enum DefaultRespCode implements RespCode {
    SUCCESS(0, "success"),
    SERVER_ERROR(500, "ServerError"),

    EMPTY_DATA(6000, "Empty Data"),
    SESSION_NOT_FOUND(6001, "Session Not Found"),
    ;

    private final int code;
    private final String msg;

    DefaultRespCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String msg() {
        return msg;
    }
}
