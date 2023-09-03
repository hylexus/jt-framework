package io.github.hylexus.jt.core.model.value;

public enum DefaultRespCode implements RespCode {
    SUCCESS(0, "success"),
    SERVER_ERROR(500, "ServerError"),
    REMOTE_CALL_ERROR(5001, "Remote call error"),

    PARAMETER_ERROR(400, "Parameter error"),
    NOT_FOUND(404, "Empty Data"),
    SESSION_NOT_FOUND(6001, "Session Not Found"),
    SEND_COMMAND_FAILURE(6003, "Command send error"),
    NO_AVAILABLE_SERVER_INSTANCE(7001, "No Available Server");

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
